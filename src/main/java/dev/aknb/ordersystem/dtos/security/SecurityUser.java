package dev.aknb.ordersystem.dtos.security;

import dev.aknb.ordersystem.models.UserStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Clock;
import java.time.Instant;
import java.util.Collection;
import java.util.Collections;

@Getter
@Setter
public class SecurityUser implements UserDetails {

    private Long id;
    private String fullName;

    private String email;

    private String password;

    private String phoneNumber;

    private String address;

    private UserStatus status;
    private Instant passwordChangedDate = Instant.now(Clock.systemUTC());

    private SecurityRole role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(role);
    }

    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {

        return this.status.equals(UserStatus.APPROVED);
    }
}
