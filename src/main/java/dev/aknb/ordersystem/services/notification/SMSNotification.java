package dev.aknb.ordersystem.services.notification;

import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class SMSNotification implements Notification {

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

    }
}
