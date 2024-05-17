package org.toffee.util;

import java.util.Properties;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.InternetAddress;
import javax.mail.Transport;
import javax.mail.Message;
import javax.mail.MessagingException;

public class MailService {
  public void sendEmail(String recEmail, String subject, String body) {
    return;
//    String from = null;
//    String password = null;
//    Properties props = System.getProperties();
//    props.put("mail.smtp.host", "smtp.gmail.com");
//    props.put("mail.smtp.port", "465");
//    props.put("mail.smtp.debug", "true");
//    props.put("mail.smtp.auth", "true");
//    props.put("mail.smtp.ssl.enable", "true");
//    Session session = Session.getInstance(props, new javax.mail.Authenticator() {
//      protected PasswordAuthentication getPasswordAuthentication() {
//        return new PasswordAuthentication(from, password);
//      }
//    });
//    session.setDebug(true);
//    try {
//      MimeMessage msg = new MimeMessage(session);
//      msg.setFrom(new InternetAddress(from));
//      msg.addRecipient(Message.RecipientType.TO, new InternetAddress(recEmail));
//      msg.setSubject(subject);
//      msg.setText(body);
//      System.out.println("Sending mail to " + recEmail + "...");
//      Transport.send(msg);
//      System.out.println("Mail successfully sent");
//    }
//    catch (MessagingException mex) {
//      mex.printStackTrace();
//    }
  }

  public String sendOTP(String recEmail, String subject) {
    // Generate random 4-digit number and send it to recEmail as OTP
    int x = (int)(Math.random() * 9000 + 1000);
    String otp = String.valueOf(x);
    String body = "Your OTP is: " + otp;
    sendEmail(recEmail, subject, body);
    return otp;
  }

}
