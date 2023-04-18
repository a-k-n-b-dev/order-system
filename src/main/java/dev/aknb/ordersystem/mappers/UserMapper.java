package dev.aknb.ordersystem.mappers;

import dev.aknb.ordersystem.dtos.auth.SignupDto;
import dev.aknb.ordersystem.dtos.security.SecurityUser;
import dev.aknb.ordersystem.dtos.user.UserDto;
import dev.aknb.ordersystem.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    SecurityUser toSecurityUser(User source);

    UserDto toUserDto(User source);
    UserDto toUserDto(SecurityUser source);

    @Mapping(target = "password", ignore = true)
    void update(@MappingTarget User target, SignupDto source);
}
