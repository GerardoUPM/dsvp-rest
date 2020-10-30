package edu.ctb.upm.midas.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;


@Service
public class EmailService {

    private static final String EMAIL_TO = "alejandro.rg@upm.es, lucia.prieto.santamaria@upm.es";
    private final static Calendar calendar = Calendar.getInstance();
    private static SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
    private static final String EMAIL_SUBJECT = "DISNET, Insercion de Datos de Wikipedia (" + formatter.format(calendar.getTime()) + ").";
    private static final String EMAIL_TEXT = "Este correo electrónico es para informar que se ha realizado de forma " +
            "correcta la extracción de datos de Wikipedia con fecha " + formatter.format(calendar.getTime()) + ".";

    @Autowired
    public EmailService() {

    }


    public static void sendEmail() {

        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.port",587);
        properties.put("mail.smtp.mail.sender","medal@ctb.upm.es");
        properties.put("mail.smtp.user", "medal@ctb.upm.es");
        properties.put("mail.smtp.auth", "true");

        Session session = Session.getDefaultInstance(properties);
        MimeMessage msg = new MimeMessage(session);
        try {
            // from
            msg.setFrom(new InternetAddress((String)properties.get("mail.smtp.mail.sender")));
            // to
            msg.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(EMAIL_TO, false));
//            // cc
//            msg.setRecipients(Message.RecipientType.CC,
//                    InternetAddress.parse(EMAIL_TO_CC, false));
            // subject
            msg.setSubject(EMAIL_SUBJECT);
            // content
            msg.setText(EMAIL_TEXT);
            msg.setSentDate(new Date());
            // Get SMTPTransport
            Transport t = session.getTransport("smtp");
            // connect
            t.connect("smtp.gmail.com","medal@ctb.upm.es", "i2IeV_#^8y!u");
            // send
            t.sendMessage(msg, msg.getAllRecipients());
            t.close();

        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }

}