package com.improve10.loginregister;

import android.util.Log;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailSender {

    private static final String EMAIL = "kalyani30082004@gmail.com"; // Replace with your Gmail address
    private static final String PASSWORD = "qfmwstmspngxdjxk"; // Replace with your Gmail app password

    public static void sendConfirmationEmail(String senderEmail, String recipientEmail, String subject, String content) {
        Thread thread = new Thread(() -> {
            try {
                // Email properties
                Properties properties = new Properties();
                properties.put("mail.smtp.host", "smtp.gmail.com");
                properties.put("mail.smtp.port", "465");
                properties.put("mail.smtp.ssl.enable", "true");
                properties.put("mail.smtp.auth", "true");

                // Session to authenticate the host sending the email
                Session session = Session.getInstance(properties, new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(EMAIL, PASSWORD);
                    }
                });

                // Create a MimeMessage object
                MimeMessage mimeMessage = new MimeMessage(session);
                mimeMessage.setFrom(new InternetAddress(EMAIL));
                mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));
                mimeMessage.setSubject(subject);
                mimeMessage.setText(content);

                // Send email
                Transport.send(mimeMessage);
                Log.d("EmailSender", "Email sent successfully to " + recipientEmail);

            } catch (MessagingException e) {
                e.printStackTrace();
                Log.e("EmailSender", "Failed to send email: " + e.getMessage());
            }
        });

        thread.start();
    }
}