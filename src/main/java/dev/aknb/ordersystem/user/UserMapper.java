package dev.aknb.ordersystem.user;

import dev.aknb.ordersystem.security.SecurityUser;
import dev.aknb.ordersystem.user.auth.dto.SignupDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    SecurityUser toSecurityUser(User source);

    @Mapping(target = "password", ignore = true)
    void update(@MappingTarget User target, SignupDto source);
}
