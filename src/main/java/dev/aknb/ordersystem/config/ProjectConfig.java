package dev.aknb.ordersystem.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class ProjectConfig {

    public static final String NAME = "Order System";
    public static final String BASE_PACKAGE = "dev.aknb.ordersystem";

    @Value("${server.mail.from}")
    private String serverMailFrom;
    @Value("${server.mail.from-name}")
    private String serverMailFromName;
    @Value("${server.mail.reply-to}")
    private String serverMailReplyTo;
    @Value("${server.url}")
    private String serverUrl;
    @Value("${server.verify-mail-url}")
    private String serverVerifyMailUrl;
    @Value("${server.reset-pass-url}")
    private String serverResetPassUrl;
    @Value("${server.approve-url}")
    private String serverApproveUrl;
    @Value("${server.login-url}")
    private String serverLoginUrl;
}
