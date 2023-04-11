package dev.aknb.ordersystem.services.notification;

import dev.aknb.ordersystem.models.RestException;
import dev.aknb.ordersystem.models.MessageType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NotificationFactory {
    private final MailNotification mailNotification;
    private final SMSNotification smsNotification;

    public NotificationFactory(MailNotification mailNotification, SMSNotification smsNotification) {
        this.mailNotification = mailNotification;
        this.smsNotification = smsNotification;
    }

    public Notification createNotification(NotificationType type) {

        switch (type) {
            case SMS -> {
                return smsNotification;
            }
            case EMAIL -> {
                return mailNotification;
            }
            default -> throw RestException.restThrow("Type", type.name(), MessageType.ERROR.name());
        }
    }
}
