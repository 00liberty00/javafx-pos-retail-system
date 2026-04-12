/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.mail;

import java.io.UnsupportedEncodingException;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import ml.util.HibernateUtil;

/**
 *
 * @author Dave
 */
public class NewMail {

    static final String ENCODING = "UTF-8";

    public void main(String email, String content) {
        String subject = "ML";
        String smtpHost = "mail.applix.top";
        String address = "ml@applix.top";
        String login = "ml@applix.top";
        String password = "ktyxbr88";
        String smtpPort = "25:465:587";

        String attachment = "src/ml/resources/log.html";

        HibernateUtil hu = new HibernateUtil();
        String nameHost = hu.getConfigHibernate();
        try {
            sendSimpleMessage(login, password, address, email, content, subject, smtpPort, smtpHost);
//            sendMultiMessage(login, password, address, email, content, subject, attachment, smtpPort, smtpHost);
            sendLogMessage(login, password, address, email, content, subject + " "+ nameHost, attachment, smtpPort, smtpHost, nameHost);

        } catch (MessagingException me) {

        } catch (UnsupportedEncodingException ue) {

        }
        //File f = new File("src/main/resources/check.json");
        //f.delete();
    }

    public void sendLoyaltyCard(String email, String content) {
        String subject = "ML";
        String smtpHost = "mail.applix.top";
        String address = "ml@applix.top";
        String login = "ml@applix.top";
        String password = "ktyxbr88";
        String smtpPort = "25:465:587";

        try {
            sendSimpleMessage(login, password, address, email, content, subject, smtpPort, smtpHost);
//            sendMultiMessage(login, password, address, email, content, subject, attachment, smtpPort, smtpHost);
        } catch (MessagingException me) {

        } catch (UnsupportedEncodingException ue) {

        }
        //File f = new File("src/main/resources/check.json");
        //f.delete();
    }

    public static void sendSimpleMessage(String login, String password, String from, String to, String content, String subject, String smtpPort, String smtpHost) throws MessagingException, UnsupportedEncodingException {
        Properties props = System.getProperties();
        props.put("mail.smtp.port", smtpPort);
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.auth", "true");
        props.put("mail.mime.charset", ENCODING);

        Authenticator auth = new MyAuthenticator(login, password);
        Session session = Session.getDefaultInstance(props, auth);

        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(from));
        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
        msg.setSubject(subject);
        msg.setContent(content, "text/html; charset=utf-8");
        Transport.send(msg);
    }

    public static void sendMultiMessage(String login, String password, String from, String to, String content, String subject, String attachment, String smtpPort, String smtpHost) throws MessagingException, UnsupportedEncodingException {
        Properties props = System.getProperties();
        props.put("mail.smtp.port", smtpPort);
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.auth", "true");
        props.put("mail.mime.charset", ENCODING);

        Authenticator auth = new MyAuthenticator(login, password);
        Session session = Session.getDefaultInstance(props, auth);

        MimeMessage msg = new MimeMessage(session);

        msg.setFrom(new InternetAddress(from));
        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
        msg.setSubject(subject, ENCODING);

        BodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(content, "text/plain; charset=" + ENCODING + "");
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);

        MimeBodyPart attachmentBodyPart = new MimeBodyPart();
        DataSource source = new FileDataSource(attachment);
        attachmentBodyPart.setDataHandler(new DataHandler(source));
        attachmentBodyPart.setFileName(MimeUtility.encodeText(source.getName()));
        multipart.addBodyPart(attachmentBodyPart);

        msg.setContent(multipart);

        Transport.send(msg);
    }

    public static void sendLogMessage(String login, String password, String from, String to, String content, String subject, String attachment, String smtpPort, String smtpHost, String nameHost) throws MessagingException, UnsupportedEncodingException {
        Properties props = System.getProperties();
        props.put("mail.smtp.port", smtpPort);
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.auth", "true");
        props.put("mail.mime.charset", ENCODING);

        Authenticator auth = new MyAuthenticator(login, password);
        Session session = Session.getDefaultInstance(props, auth);

        MimeMessage msg = new MimeMessage(session);

        msg.setFrom(new InternetAddress(from));
        msg.setRecipient(Message.RecipientType.TO, new InternetAddress("00liberty00@gmail.com"));
        msg.setSubject("ML_LOG" + " / " + nameHost, ENCODING);

        BodyPart messageBodyPart = new MimeBodyPart();
//                content = "Лог файл от " + nameHost;

        content = "Лог файл от Izob";
        messageBodyPart.setContent(content, "text/plain; charset=" + ENCODING + "");
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);

        MimeBodyPart attachmentBodyPart = new MimeBodyPart();
        DataSource source = new FileDataSource(attachment);
        attachmentBodyPart.setDataHandler(new DataHandler(source));
        attachmentBodyPart.setFileName(MimeUtility.encodeText(source.getName()));
        multipart.addBodyPart(attachmentBodyPart);

        msg.setContent(multipart);

        Transport.send(msg);
    }
}

class MyAuthenticator extends Authenticator {

    private String user;
    private String password;

    MyAuthenticator(String user, String password) {
        this.user = user;
        this.password = password;
    }

    public PasswordAuthentication getPasswordAuthentication() {
        String user = this.user;
        String password = this.password;
        return new PasswordAuthentication(user, password);
    }
}
