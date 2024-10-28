package ru.javaops.masterjava.service;

import org.junit.Test;
import ru.javaops.masterjava.xml.schema.User;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

public class MainXmlTest {
    private MainXml mainXml = new MainXml();
    @Test
    public void getAllStudentsFromProject() throws JAXBException, IOException {
        List<User> masterJavaStudents = mainXml.getAllStudentsFromProject("masterjava");
        assertEquals(masterJavaStudents.size(), 2);
    }
}