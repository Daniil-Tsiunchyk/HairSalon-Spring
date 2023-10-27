package bsuir.coursework.HairSalon.services;

import jakarta.mail.Message;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

  private final Session mailSession;

  @Autowired
  public EmailService(Session mailSession) {
    this.mailSession = mailSession;
  }

  public void sendResetCodeByEmail(String email, String resetCode) {
    new Thread(() -> {
      try {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date now = new Date();
        Date expiration = new Date(now.getTime() + 5 * 60 * 1000);

        Message message = new MimeMessage(mailSession);
        message.setFrom(new InternetAddress("lc.devsparkclub@yandex.by"));
        message.setRecipient(
          Message.RecipientType.TO,
          new InternetAddress(email)
        );
        message.setSubject("Password Reset Code");
        message.setText(
          "Your password reset code is: " +
          resetCode +
          "\nExpires at: " +
          sdf.format(expiration)
        );
        Transport.send(message);
      } catch (Exception e) {
        e.printStackTrace();
        System.err.println("Failed to send reset code email to: " + email);
      }
    })
      .start();
  }
}
