package org.uvigo.esei.unio.ui;

import android.os.Bundle;

import org.uvigo.esei.unio.R;
import org.uvigo.esei.unio.core.MailManager;

public class MailServiceActivity extends ServiceActivity {

    private MailManager mailManager;
    private final String MAIL_ADDRESS = "unio.user.esei@gmail.com";
    private final String MAIL_PASS = "unio.user.esei.2020";

    private final String MAIL_WECOLME = "In order to send a mail use this format:\n" +
            "valid@mail.com : subject\n" +
            "The email that you wan to send";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mailManager = new MailManager(MAIL_ADDRESS, MAIL_PASS);
        sendMessage(getString(R.string.mail_welcome));
    }


    @Override
    protected void processUserInput() {
        String newMessage = super.getAndSendUserInput();
        if (!newMessage.equals("")) {

            /* EMAIL TEMPLATE:
             *
             *       **************************************************
             *       *   cmrodriguez17@esei.uvigo.es : Email subject  *
             *       *   Email body                                   *
             *       **************************************************
             */

            try {

                String[] dividedMessage = newMessage.split("\n", 2);
                String[] dividedFirstLine = dividedMessage[0].split(":", 2);

                String to = dividedFirstLine[0].trim();
                String subject = dividedFirstLine[1].trim();
                if (subject.equals("")) {
                    subject = getString(R.string.sent_mail);
                }
                String body = dividedMessage[1];

                mailManager.sendMail(to, subject, body);
                sendMessage(getString(R.string.sent_mail));

            } catch (ArrayIndexOutOfBoundsException exception) {
                sendMessage(getString(R.string.incorrect_mail_format));
            } catch (MailManager.MailManagerException exception) {
                sendMessage(getString(R.string.incorrect_mail_format));
            }
        }
    }
}
