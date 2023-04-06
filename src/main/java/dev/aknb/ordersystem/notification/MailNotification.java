package dev.aknb.ordersystem.notification;

import dev.aknb.ordersystem.config.ProjectConfig;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.charset.StandardCharsets;

@Slf4j
@Service
public class MailNotification implements Notification {

    private final JavaMailSender javaMailSender;
    private final ProjectConfig serverConfig;

    public MailNotification(JavaMailSender javaMailSender, ProjectConfig serverConfig) {
        this.javaMailSender = javaMailSender;
        this.serverConfig = serverConfig;
    }

    @Override
    public void sendNotification(String to, String content) {
        this.sendNotification(to, content, null, null);
    }

    @Override
    public void sendNotification(String to, String content, String subject) {
        this.sendNotification(to, content, subject, null);
    }

    @Override
    public void sendNotification(String to, String content, String subject, File file) {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, StandardCharsets.UTF_8.name());
            message.setTo(to);
            message.setFrom(serverConfig.getServerMailFrom(), serverConfig.getServerMailFromName());
            message.setSubject(subject);
            message.setText(content, true);
            if (file != null) {
                message.addAttachment(file.getName(), file);
            }
            javaMailSender.send(mimeMessage);
            log.info("Sent email to: {} with subject: {}", to, subject);
        } catch (Exception e) {
            log.error("Failed to send email to: {} with error: {}", to, e.getMessage());
        }
    }
}
