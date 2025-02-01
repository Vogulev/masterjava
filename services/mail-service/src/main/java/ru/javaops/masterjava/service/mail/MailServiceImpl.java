package ru.javaops.masterjava.service.mail;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import ru.javaops.masterjava.service.mail.dao.MailDao;
import ru.javaops.masterjava.service.mail.model.MailModel;

import javax.jws.WebService;
import javax.mail.internet.InternetAddress;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@WebService(endpointInterface = "ru.javaops.masterjava.service.mail.MailService")
public class MailServiceImpl implements MailService {

    private static final Email email = EmailProvider.initEmail();

    public void sendMail(List<Addressee> to, List<Addressee> cc, String subject, String body) {
        val sendTo = to.stream()
                .map(MailServiceImpl::mapAddressee)
                .collect(Collectors.toList());
        val sendCc = cc.stream()
                .map(MailServiceImpl::mapAddressee)
                .collect(Collectors.toList());
        email.setSubject(subject);
        email.setContent(body, "text/html; charset=utf-8");
        String result = "";
        String emailTo = to.stream().map(Addressee::getEmail).collect(Collectors.joining(","));
        String emailCc = cc.stream().map(Addressee::getEmail).collect(Collectors.joining(","));
        boolean success = true;
        try {
            if (!cc.isEmpty()) {
                email.setCc(sendCc);
            }
            email.setTo(sendTo);
            result = email.send();
        } catch (EmailException e) {
            log.error(e.getMessage());
            success = false;
        } finally {
            MailDao dao = DBIProvider.getDao(MailDao.class);
            MailModel mailModel = MailModel.builder()
                    .success(success)
                    .result(result)
                    .body(body)
                    .subject(subject)
                    .to(emailTo)
                    .cc(emailCc).build();
            dao.saveMailResult(mailModel);
        }
    }

    private static InternetAddress mapAddressee(Addressee addressee) {
        try {
            return new InternetAddress(addressee.getEmail(), addressee.getName());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}