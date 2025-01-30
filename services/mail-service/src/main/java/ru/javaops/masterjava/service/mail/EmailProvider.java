package ru.javaops.masterjava.service.mail;

import com.typesafe.config.Config;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import ru.javaops.masterjava.config.Configs;

public class EmailProvider {

    public static Email initEmail() {
        Config mail = Configs.getConfig("mail.conf", "mail");
        return initMail(mail.getString("host"), mail.getString("port"),
                mail.getString("username"), mail.getString("password"),
                mail.getString("fromName"), mail.getBoolean("debug"),
                mail.getBoolean("useSSL"), mail.getBoolean("useTLS"));
    }

    private static Email initMail(String host, String port, String username, String password, String fromName,
                                  boolean debug, boolean useSSL, boolean useTLS) {
        Email email = new SimpleEmail();
        email.setHostName(host);
        email.setSslSmtpPort(port);
        email.setAuthentication(username, password);
        email.setSSLOnConnect(useSSL);
        email.setStartTLSEnabled(useTLS);
        email.setDebug(debug);
        try {
            email.setFrom(username, fromName);
        } catch (EmailException e) {
            throw new RuntimeException(e);
        }
        return email;
    }
}