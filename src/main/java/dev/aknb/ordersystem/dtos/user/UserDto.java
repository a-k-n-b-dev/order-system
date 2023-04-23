package dev.aknb.ordersystem.dtos.user;

import dev.aknb.ordersystem.models.UserStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {

    private Long id;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String address;
    private UserStatus status;
}
