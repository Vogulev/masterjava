package ru.javaops.masterjava.service.mail;

import com.typesafe.config.Config;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import ru.javaops.masterjava.config.Configs;

public class EmailProvider {
    public static void initMail() throws EmailException {
        Config mail = Configs.getConfig("mail.conf", "mail");
        initMail(mail.getString("host"), mail.getString("port"),
                mail.getString("username"), mail.getString("password"),
                mail.getString("fromName"), mail.getBoolean("debug"),
                mail.getBoolean("useSSL"), mail.getBoolean("useTLS"));
    }

    public static void initMail(String host, String port, String username, String password, String fromName,
                                boolean debug, boolean useSSL, boolean useTLS) throws EmailException {
        Email email = new SimpleEmail();
        email.setHostName(host);
        email.setSslSmtpPort(port);
        email.setAuthentication(username, password);
        email.setSSLOnConnect(useSSL);
        email.setStartTLSEnabled(useTLS);
        email.setFrom(username, fromName);
        email.setDebug(debug);
    }
}