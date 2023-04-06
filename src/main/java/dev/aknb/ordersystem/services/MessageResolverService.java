package dev.aknb.ordersystem.services;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class MessageResolverService {

    private final MessageSource messageSource;

    public MessageResolverService(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String getMessage(String code) {
        return this.getMessage(code, null, LocaleContextHolder.getLocale());
    }

    public String getMessage(String code, Object[] params) {
        return this.getMessage(code, params, LocaleContextHolder.getLocale());
    }

    public String getMessage(String code, Object[] params, Locale locale) {
        return messageSource.getMessage(code, params, locale);
    }
}
