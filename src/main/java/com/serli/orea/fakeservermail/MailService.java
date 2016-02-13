package com.serli.orea.fakeservermail;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * Created by gpoulet on 11/02/2016.
 */
public class MailService {

    public static final String LOGIN = "login_google_without_at_gmail_dot_com";
    public static final String PASSWORD = "google_password";
    public static final String FROM = "bob@serli.com";
    public static final String TO = "roger@serli.com";
    public static final String SUBJECT = "Test Email Service Orea";

    public void send(InputStream requestBody) {

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(LOGIN, PASSWORD);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(TO));
            message.setSubject(SUBJECT);

            try {
                message.setText(read(requestBody));
            } catch (IOException e) {
                e.printStackTrace();
            }

            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

        public static String read(InputStream input) throws IOException {
            try (BufferedReader buffer = new BufferedReader(new InputStreamReader(input))) {
                String collect = buffer.lines().collect(Collectors.joining("\n"));
                System.out.println("InputStream : "+ input);
                return collect;
            }

        }

}
