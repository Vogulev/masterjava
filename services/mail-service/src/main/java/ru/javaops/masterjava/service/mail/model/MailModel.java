package ru.javaops.masterjava.service.mail.model;

import lombok.*;

@Data
@Builder
@ToString(callSuper = true)
public class MailModel {
    private boolean success;
    private String to;
    private String cc;
    private String subject;
    private String body;
    private String result;
}
