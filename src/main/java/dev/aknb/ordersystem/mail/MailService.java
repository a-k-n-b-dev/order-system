package dev.aknb.ordersystem.mail;

import dev.aknb.ordersystem.message.MessageResolver;
import dev.aknb.ordersystem.message.MessageType;
import dev.aknb.ordersystem.notification.Notification;
import dev.aknb.ordersystem.notification.NotificationFactory;
import dev.aknb.ordersystem.notification.NotificationType;
import dev.aknb.ordersystem.project.ProjectConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Slf4j
@Service
public class MailService {

    private final SpringTemplateEngine templateEngine;
    private final MessageResolver messageResolver;
    private final ProjectConfig projectConfig;
    private final Notification notification;


    public MailService(SpringTemplateEngine templateEngine, MessageResolver messageResolver, ProjectConfig projectConfig, NotificationFactory notificationFactory) {
        this.templateEngine = templateEngine;
        this.messageResolver = messageResolver;
        this.projectConfig = projectConfig;
        notification = notificationFactory.createNotification(NotificationType.EMAIL);
    }

    @Async
    public void sendVerifyToken(String email, String token) {

        String subject = messageResolver.getMessage(MessageType.EMAIL_VERIFICATION.name());
        Context context = new Context();
        context.setVariable("serverUrl", projectConfig.getServerUrl());
        context.setVariable("subject", subject);
        context.setVariable("token", token);
        notification.sendNotification(email, templateEngine.process("verifyToken", context), subject);
    }
}
