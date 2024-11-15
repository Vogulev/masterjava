package ru.javaops.masterjava.xml;

import j2html.tags.ContainerTag;
import ru.javaops.masterjava.xml.schema.User;
import ru.javaops.masterjava.xml.util.JaxbParser;
import ru.javaops.masterjava.xml.util.StaxStreamProcessor;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.io.IOException;
import java.util.Collection;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import static j2html.TagCreator.*;

@WebServlet("/upload/")
@MultipartConfig(location = "/")
public class FileUploadServlet extends HttpServlet {

    private static final Comparator<User> USER_COMPARATOR = Comparator.comparing(User::getValue).thenComparing(User::getEmail);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        getServletContext().getRequestDispatcher("/WEB-INF/jsp/upload.jsp").forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Collection<Part> parts = req.getParts();
        Set<User> users = new TreeSet<>(USER_COMPARATOR);
        for (Part part : parts) {
            try (StaxStreamProcessor processor = new StaxStreamProcessor(part.getInputStream())) {
                JaxbParser parser = new JaxbParser(User.class);
                while (processor.doUntil(XMLEvent.START_ELEMENT, "User")) {
                    User user = parser.unmarshal(processor.getReader(), User.class);
                    users.add(user);
                }
            } catch (XMLStreamException | JAXBException e) {
                throw new RuntimeException(e);
            }
        }
        req.setAttribute("users", toHtml(users));
        req.getRequestDispatcher("/WEB-INF/jsp/users.jsp").forward(req, resp);
    }

    private static String toHtml(Set<User> users) {
        final ContainerTag table = table().with(
                        tr().with(th("FullName"), th("email"), th("flag")))
                .attr("border", "1")
                .attr("cellpadding", "8")
                .attr("cellspacing", "0");

        users.forEach(u -> table.with(tr().with(td(u.getValue()), td(u.getEmail()), td(u.getFlag().value()))));

        return html().with(
                head().with(title("users")),
                body().with(h1("users"), table)
        ).render();
    }
}
