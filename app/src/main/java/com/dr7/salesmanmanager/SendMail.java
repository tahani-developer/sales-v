package com.dr7.salesmanmanager;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Security;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import android.util.Log;



import java.security.Security;
import java.util.Properties;

public class SendMail extends javax.mail.Authenticator {
    private String mailhost = "smtp.gmail.com";
    private String user;
    private Session session;
//    final  String emailSender="ta.email.2022.fa@gmail.com";
//     String password="tafa2022";
    final  String emailSender="1234developersteam@gmail.com";
    String password="androiddevelopers4";
    private Multipart _multipart = new MimeMultipart();

    static {
        Security.addProvider(new JSSEProvider());
    }

    public SendMail() {
        user = emailSender;


        Properties props = new Properties();

        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.host", mailhost);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.quitwait", "false");

        session = Session.getDefaultInstance(props, this);
    }

    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(user, password);
    }

    public synchronized void sendMail(String subject, String body, String sender, String recipients) throws Exception {
        try {
            Log.e("sendMail","2"+sender);
            MimeMessage message = new MimeMessage(session);
            DataHandler handler = new DataHandler(new ByteArrayDataSource(body.getBytes(), "text/plain"));
            message.setSender(new InternetAddress(sender));
            message.setSubject(subject);
            message.setDataHandler(handler);

            BodyPart messageBodyPart = new MimeBodyPart();

            messageBodyPart.setText(body);

            _multipart.addBodyPart(messageBodyPart);



            // Put parts in message

            message.setContent(_multipart);



            if (recipients.indexOf(',') > 0)
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipients));
            else
                message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipients));
            Transport.send(message);
            Log.e("SEND ", "SEND");
        } catch (Exception e) {
            Log.e("ERROR ", "ERROR"+e.getMessage());
            e.printStackTrace();
        }
    }
}
//
//    String messageSend="";
//
//    public SendMail(String messageSend) {
//        this.messageSend = messageSend;
//    }
//    Properties properties = new Properties();
//
//    String stringHost = "smtp.gmail.com";
//            properties.put("mail.smtp.host", "smtp.gmail.com");
//            properties.put("mail.smtp.port", "465");
//            properties.put("mail.smtp.ssl.enable", "true");
//            properties.put("mail.smtp.auth", "true");
