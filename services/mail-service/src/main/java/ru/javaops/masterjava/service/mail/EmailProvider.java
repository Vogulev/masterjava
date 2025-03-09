package ru.javaops.masterjava.service.mail;

import com.typesafe.config.Config;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import ru.javaops.masterjava.config.Configs;

public class EmailProvider {

    public static Email initEmail() {
        Config mail = Configs.getConfig("mail.conf", "mail");
        return initMail(mail);
    }

    private static Email initMail(Config mail) {
        Email email = new SimpleEmail();
        email.setHostName(mail.getString("host"));
        email.setSslSmtpPort(mail.getString("port"));
        email.setAuthentication(mail.getString("username"), mail.getString("password"));
        email.setSSLOnConnect(mail.getBoolean("useSSL"));
        email.setStartTLSEnabled(mail.getBoolean("useTLS"));
        email.setDebug(mail.getBoolean("debug"));
        try {
            email.setFrom(mail.getString("username"), mail.getString("fromName"));
        } catch (EmailException e) {
            throw new RuntimeException(e);
        }
        return email;
    }
}