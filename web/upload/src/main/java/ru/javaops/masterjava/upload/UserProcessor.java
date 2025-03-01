package ru.javaops.masterjava.upload;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.persist.dao.GroupDao;
import ru.javaops.masterjava.persist.dao.UserDao;
import ru.javaops.masterjava.persist.dao.UserGroupDao;
import ru.javaops.masterjava.persist.model.City;
import ru.javaops.masterjava.persist.model.Group;
import ru.javaops.masterjava.persist.model.User;
import ru.javaops.masterjava.persist.model.UserGroup;
import ru.javaops.masterjava.persist.model.type.UserFlag;
import ru.javaops.masterjava.upload.PayloadProcessor.FailedEmails;
import ru.javaops.masterjava.xml.schema.ObjectFactory;
import ru.javaops.masterjava.xml.util.JaxbParser;
import ru.javaops.masterjava.xml.util.StaxStreamProcessor;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Slf4j
public class UserProcessor {
    private static final int NUMBER_THREADS = 4;

    private static final JaxbParser jaxbParser = new JaxbParser(ObjectFactory.class);
    private static UserDao userDao = DBIProvider.getDao(UserDao.class);
    private static GroupDao groupDao = DBIProvider.getDao(GroupDao.class);
    private static UserGroupDao userGroupDao = DBIProvider.getDao(UserGroupDao.class);

    private ExecutorService executorService = Executors.newFixedThreadPool(NUMBER_THREADS);

    /*
     * return failed users chunks
     */
    public List<FailedEmails> process(final StaxStreamProcessor processor, Map<String, City> cities, int chunkSize) throws XMLStreamException, JAXBException {
        log.info("Start processing with chunkSize=" + chunkSize);

        Map<String, Future<Map<Integer, String>>> chunkFutures = new LinkedHashMap<>();  // ordered map (emailRange -> chunk future)

        int id = userDao.getSeqAndSkip(chunkSize);
        List<User> chunk = new ArrayList<>(chunkSize);
        val unmarshaller = jaxbParser.createUnmarshaller();
        List<FailedEmails> failed = new ArrayList<>();

        Map<String, Group> groupsFromDb = groupDao.getAsMap();
        List<UserGroup> groupChunk = new ArrayList<>(chunkSize);

        while (processor.doUntil(XMLEvent.START_ELEMENT, "User")) {
            String cityRef = processor.getAttribute("city");  // unmarshal doesn't get city ref
            String groupRefs = processor.getAttribute("groupRefs");
            List<String> userGroups = Collections.emptyList();
            if (groupRefs != null) {
                userGroups = Arrays.asList(groupRefs.split(" "));
            }
            ru.javaops.masterjava.xml.schema.User xmlUser = unmarshaller.unmarshal(processor.getReader(), ru.javaops.masterjava.xml.schema.User.class);
            if (cities.get(cityRef) == null) {
                failed.add(new FailedEmails(xmlUser.getEmail(), "City '" + cityRef + "' is not present in DB"));
            } else {
                for (val groupRef : userGroups) {
                    Group group = groupsFromDb.get(groupRef);
                    if (group == null) {
                        log.error("Group {} not found in DB", groupRef);
                    } else {
                        groupChunk.add(new UserGroup(id, group.getId()));
                    }
                }
                final User user = new User(id++, xmlUser.getValue(), xmlUser.getEmail(), UserFlag.valueOf(xmlUser.getFlag().value()), cityRef);
                chunk.add(user);
                if (chunk.size() == chunkSize) {
                    addChunkFutures(chunkFutures, chunk);
                    chunk = new ArrayList<>(chunkSize);
                    id = userDao.getSeqAndSkip(chunkSize);
                    groupChunk = new ArrayList<>(chunkSize);
                }
            }
        }

        if (!chunk.isEmpty()) {
            addChunkFutures(chunkFutures, chunk);
        }

        Map<Integer, String> allAlreadyPresents = new HashMap<>();
        chunkFutures.forEach((emailRange, future) -> {
            try {
                Map<Integer, String> alreadyPresentsInChunk = future.get();
                log.info("{} successfully executed with already presents: {}", emailRange, alreadyPresentsInChunk);
                allAlreadyPresents.putAll(alreadyPresentsInChunk);
            } catch (InterruptedException | ExecutionException e) {
                log.error("{} failed", emailRange, e);
                failed.add(new FailedEmails(emailRange, e.toString()));
            }
        });
        if (!allAlreadyPresents.isEmpty()) {
            failed.add(new FailedEmails(allAlreadyPresents.values().toString(), "already presents"));
        }
        List<UserGroup> collect = groupChunk.stream()
                .filter(userGroup -> !allAlreadyPresents.containsKey(userGroup.getUserId()))
                .collect(Collectors.toList());
        userGroupDao.insertBatch(collect);
        return failed;
    }

    private void addChunkFutures(Map<String, Future<Map<Integer, String>>> chunkFutures, List<User> chunk) {
        String emailRange = String.format("[%s-%s]", chunk.get(0).getEmail(), chunk.get(chunk.size() - 1).getEmail());
        Future<Map<Integer, String>> future = executorService.submit(() -> userDao.insertAndGetConflictEmails(chunk));
        chunkFutures.put(emailRange, future);
        log.info("Submit chunk: " + emailRange);
    }
}