package dev.aknb.ordersystem.services;

import dev.aknb.ordersystem.config.ProjectConfig;
import dev.aknb.ordersystem.models.MessageType;
import dev.aknb.ordersystem.services.notification.Notification;
import dev.aknb.ordersystem.services.notification.NotificationFactory;
import dev.aknb.ordersystem.services.notification.NotificationType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
public class MailService {

    private final SpringTemplateEngine templateEngine;
    private final MessageResolverService messageResolverService;
    private final ProjectConfig projectConfig;
    private final Notification notification;

    @Value("${admin.mail}")
    private String adminMail;


    public MailService(SpringTemplateEngine templateEngine, MessageResolverService messageResolverService, ProjectConfig projectConfig, NotificationFactory notificationFactory) {
        this.templateEngine = templateEngine;
        this.messageResolverService = messageResolverService;
        this.projectConfig = projectConfig;
        notification = notificationFactory.createNotification(NotificationType.EMAIL);
    }

    @Async
    public void sendVerifyToken(String email, String token) {

        String subject = messageResolverService.getMessage(MessageType.EMAIL_VERIFICATION.name());
        String url = projectConfig.getServerVerifyMailUrl() + "/" + token;
        Context context = new Context();
        context.setVariable("verifyUrl", url);
        context.setVariable("subject", subject);
        notification.sendNotification(email, templateEngine.process("verifyToken", context), subject);
    }

    @Async
    public void sendResetPassword(String email, String token) {
        String subject = messageResolverService.getMessage(MessageType.RESET_PASSWORD.name());
        String url = projectConfig.getServerResetPassUrl() + "/" + token;
        Context context = new Context();
        context.setVariable("resetUrl", url);
        context.setVariable("subject", subject);
        notification.sendNotification(email, templateEngine.process("resetPassword", context), subject);
    }

    @Async
    public void sendApprove(String email, String token) {
        String subject = messageResolverService.getMessage(MessageType.APPROVE.name());
        String url = projectConfig.getServerApproveUrl() + "/" + token;
        Context context = new Context();
        context.setVariable("approveUrl", url);
        context.setVariable("subject", subject);
        context.setVariable("email", email);
        notification.sendNotification(adminMail, templateEngine.process("approve", context), subject);
    }

    @Async
    public void sendApproved(String email, String fullName) {
        String subject = messageResolverService.getMessage(MessageType.APPROVED.name());
        Context context = new Context();
        context.setVariable("loginUrl", projectConfig.getServerLoginUrl());
        context.setVariable("subject", subject);
        context.setVariable("fullName", fullName);
        notification.sendNotification(email, templateEngine.process("approved", context), subject);
    }
}
