package dev.aknb.ordersystem.services.notification;

import java.io.File;

public interface Notification {

    void sendNotification(String to, String content);
    void sendNotification(String to, String content, String subject);
    void sendNotification(String to, String content, String subject, File file);
}
