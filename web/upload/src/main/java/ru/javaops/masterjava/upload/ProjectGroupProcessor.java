package ru.javaops.masterjava.upload;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.persist.dao.GroupDao;
import ru.javaops.masterjava.persist.dao.ProjectDao;
import ru.javaops.masterjava.persist.model.Group;
import ru.javaops.masterjava.persist.model.Project;
import ru.javaops.masterjava.persist.model.type.GroupType;
import ru.javaops.masterjava.xml.util.StaxStreamProcessor;

import javax.xml.stream.XMLStreamException;

@Slf4j
public class ProjectGroupProcessor {
    private final ProjectDao projectDao = DBIProvider.getDao(ProjectDao.class);
    private final GroupDao groupDao = DBIProvider.getDao(GroupDao.class);

    public void process(StaxStreamProcessor processor) throws XMLStreamException {
        val projectMap = projectDao.getAsMap();
        val groupMap = groupDao.getAsMap();

        while (processor.startElement("Project", "Projects")) {
            val name = processor.getAttribute("name");
            Project project = projectMap.get(name);
            int projectId;
            if (project == null) {
                projectId = projectDao.insertGeneratedId(new Project(name, processor.getElementValue("description")));
                log.info("Insert project {}", projectId);
            } else {
                projectId = project.getId();
            }
            while (processor.startElement("Group", "Project")) {
                val groupName = processor.getAttribute("name");
                val groupType = processor.getAttribute("type");
                if (!groupMap.containsKey(groupName)) {
                    groupDao.insertGeneratedId(new Group(groupName, GroupType.valueOf(groupType), projectId));
                }
            }
        }
    }

}
