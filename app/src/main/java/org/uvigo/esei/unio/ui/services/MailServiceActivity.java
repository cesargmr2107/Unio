package org.uvigo.esei.unio.ui.services;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import org.uvigo.esei.unio.R;
import org.uvigo.esei.unio.core.MailManager;
import org.uvigo.esei.unio.core.SharedPreferencesManager;
import org.uvigo.esei.unio.ui.SettingsActivity;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MailServiceActivity extends InternetServiceActivity {

    private MailManager mailManager;
    private String mailAddress;
    private String mailPass;

    public static final String MAIL_ADDRESS = "mailAdress";
    public static final String MAIL_PASS = "mailPass";

    private static EditText mailAdressET;
    private static EditText mailPassET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mailAddress = SharedPreferencesManager.getString(this, MAIL_ADDRESS);
        mailPass = SharedPreferencesManager.getEncryptedString(this, MAIL_PASS);
        mailManager = new MailManager(mailAddress, mailPass);
        if (mailAddress != null) {
            super.sendWelcomeMessage(getString(R.string.mail_welcome, mailAddress));
        } else {
            super.sendWelcomeMessage(getString(R.string.no_mail_account));
        }
    }

    @Override
    protected void performService() {
        String newMessage = super.getAndSendUserInput();
        if (!newMessage.equals("")) {
            if (mailAddress != null) {
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
        } else {
            super.sendMessage(getString(R.string.no_mail_account));
        }
    }

    @Override
    public void settings(Context context) {
        AlertDialog.Builder DLG = new AlertDialog.Builder(context);
        DLG.setView(R.layout.mail_settings);

        DLG.setPositiveButton("Save", (dialog, which) -> {
            String email = mailAdressET.getText().toString();
            SharedPreferencesManager.setString(context, MAIL_ADDRESS, email);
            String passwd = mailPassET.getText().toString();
            SharedPreferencesManager.setEncryptedString(context, MAIL_PASS, passwd);
            Toast.makeText(context, R.string.setting_saved, Toast.LENGTH_SHORT).show();
        });

        DLG.setNegativeButton("Cancel", null);

        AlertDialog alert = DLG.create();
        alert.show();

        mailAdressET = alert.findViewById(R.id.email_input);
        String mailAddress = SharedPreferencesManager.getString(context, MAIL_ADDRESS);
        mailAdressET.setText((mailAddress == null) ? "" : mailAddress);
        mailPassET = alert.findViewById(R.id.passwd_input);
    }
}

