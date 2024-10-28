package ru.javaops.masterjava.service;

import com.google.common.io.Resources;
import ru.javaops.masterjava.xml.schema.Group;
import ru.javaops.masterjava.xml.schema.ObjectFactory;
import ru.javaops.masterjava.xml.schema.Payload;
import ru.javaops.masterjava.xml.schema.User;
import ru.javaops.masterjava.xml.util.JaxbParser;
import ru.javaops.masterjava.xml.util.Schemas;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainXml {
    private static final JaxbParser JAXB_PARSER = new JaxbParser(ObjectFactory.class);

    static {
        JAXB_PARSER.setSchema(Schemas.ofClasspath("payload.xsd"));
    }

    public List<User> getAllStudentsFromProject(String projectName) throws IOException, JAXBException {
        List<User> usersInProject = new ArrayList<>();

        Payload payload = JAXB_PARSER.unmarshal(
                Resources.getResource("payload.xml").openStream());
        List<Object> groups = payload.getProjects()
                .getProject()
                .stream()
                .filter(e -> e.getName().equals(projectName))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new)
                .getGroups();

        List<User> users = payload.getUsers().getUser();

        for (User user : users) {
            for (Object group : groups) {
                if (user.getGroup().contains(group)) {
                    usersInProject.add(user);
                }
            }
        }
        return usersInProject;
    }
}
