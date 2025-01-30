package ru.javaops.masterjava.service.mail;

import lombok.val;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;

import javax.jws.WebService;
import javax.mail.internet.InternetAddress;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.stream.Collectors;

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
        try {
            if (cc.size() > 0) {
                email.setCc(sendCc);
            }
            email.setTo(sendTo);
            result = email.send();
        } catch (EmailException e) {
            throw new RuntimeException(e);
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