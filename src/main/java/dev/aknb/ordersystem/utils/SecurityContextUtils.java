package dev.aknb.ordersystem.utils;

import dev.aknb.ordersystem.dtos.security.SecurityUser;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@UtilityClass
public class SecurityContextUtils {

    public Optional<Long> getUserId() {
        Optional<SecurityUser> user = getUser();
        return user.map(SecurityUser::getId);
    }

    public Optional<String> getUserEmail() {
        Optional<SecurityUser> user = getUser();
        return user.map(SecurityUser::getEmail);
    }

    public Optional<SecurityUser> getUser() {
        SecurityContext context = getContext();
        if (context.getAuthentication() == null) {
            return Optional.empty();
        }

        Object principal = context.getAuthentication().getPrincipal();

        if (principal instanceof SecurityUser) {
            return Optional.of((SecurityUser) principal);
        }
        return Optional.empty();
    }

    SecurityContext getContext() {
        return SecurityContextHolder.getContext();
    }
}
