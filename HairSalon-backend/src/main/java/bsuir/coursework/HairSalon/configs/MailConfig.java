package bsuir.coursework.HairSalon.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import jakarta.mail.*;

import java.util.Properties;

@Configuration
public class MailConfig {

    private final String username = "lc.devsparkclub@yandex.by";

    private final String password = "xhnawltysmwjcryy";

    @Bean
    public Session mailSession() {
        Properties properties = new Properties();
        String host = "smtp.yandex.ru";
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.auth", "true");
        int port = 465;
        properties.put("mail.smtp.socketFactory.port", port);
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        return Session.getDefaultInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
    }
}