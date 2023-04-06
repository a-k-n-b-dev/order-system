package dev.aknb.ordersystem.dtos.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordDto {

    @NotBlank(message = "CURRENT_PASSWORD_NOT_BLANK")
    private String currentPassword;

    @NotBlank(message = "NEW_PASSWORD_NOT_BLANK")
    private String newPassword;
}
