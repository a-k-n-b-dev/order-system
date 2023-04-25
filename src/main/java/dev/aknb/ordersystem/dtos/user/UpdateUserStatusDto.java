package dev.aknb.ordersystem.dtos.user;

import dev.aknb.ordersystem.models.UserStatus;
import lombok.Getter;

@Getter
public class UpdateUserStatusDto {
    
    private UserStatus status;
}
