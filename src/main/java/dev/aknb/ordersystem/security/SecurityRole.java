package dev.aknb.ordersystem.security;

import dev.aknb.ordersystem.role.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SecurityRole implements GrantedAuthority {

    private RoleEnum name;

    @Override
    public String getAuthority() {
        return name.name();
    }
}
