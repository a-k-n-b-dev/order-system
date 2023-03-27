package dev.aknb.ordersystem.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {

    private Long id;
    private String fullName;
    private String email;
    private String phoneNumber;
}
