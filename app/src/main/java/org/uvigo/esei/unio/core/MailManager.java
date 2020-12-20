package org.uvigo.esei.unio.core;

import android.util.Log;

import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailManager {

    private String emailAddress;
    private String password;

    public MailManager(String emailAddress, String password) {
        this.emailAddress = emailAddress;
        this.password = password;
    }

    public void sendMail(String to, String subject, String body) throws MailManagerException {

        boolean success = true;
        try {
            Session session = Session.getDefaultInstance(configureProperties(), new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(emailAddress, password);
                }
            });

            if (session != null) {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(emailAddress));
                message.setSubject(subject);
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
                message.setContent(body, "text/html; charset=utf-8");

                Transport.send(message);
            }
        } catch (Exception exception) {
            Log.e("sending-mail-error", "ERROR: " + exception.getMessage());
            success = false;
        }

        if (!success) {
            throw new MailManagerException();
        }

    }

    private Properties configureProperties() {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.googlemail.com");
        properties.put("mail.smtp.socketFactory.port", "465");
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.port", "465");
        return properties;
    }

    public class MailManagerException extends Exception {
        public MailManagerException() {
            super();
        }
    }
}
