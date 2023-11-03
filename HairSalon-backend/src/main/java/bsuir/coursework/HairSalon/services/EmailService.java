package bsuir.coursework.HairSalon.services;

import bsuir.coursework.HairSalon.models.User;
import bsuir.coursework.HairSalon.repositories.UserRepository;
import jakarta.mail.Message;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

  private final Session mailSession;
  private final UserRepository userRepository;

  @Autowired
  public EmailService(Session mailSession, UserRepository userRepository) {
    this.mailSession = mailSession;
    this.userRepository = userRepository;
  }

  public void sendEmail(String email, String subject, String text) {
    try {
      Message message = new MimeMessage(mailSession);
      message.setFrom(new InternetAddress("lc.devsparkclub@yandex.by"));
      message.setRecipient(
        Message.RecipientType.TO,
        new InternetAddress(email)
      );
      message.setSubject(subject);
      message.setText(text);
      Transport.send(message);
    } catch (Exception e) {
      System.err.println("Failed to send email to: " + email);
    }
  }

  public void sendSpam(
    String subject,
    String text,
    User.UserRole role
  ) {

      List<User> users = userRepository.findAll();
      for (User user : users) {
        if (user.getRole().equals(role)) {
          try {
          Message message = new MimeMessage(mailSession);
          message.setFrom(new InternetAddress("lc.devsparkclub@yandex.by"));
          message.setRecipient(
            Message.RecipientType.TO,
            new InternetAddress(user.getEmail())
          );
          message.setSubject(subject);
          message.setText(text);
          Transport.send(message);
          } catch (Exception e) {
            System.err.println("Failed to send email to: " + user.getEmail());
          }
        }
      }
  }
}
