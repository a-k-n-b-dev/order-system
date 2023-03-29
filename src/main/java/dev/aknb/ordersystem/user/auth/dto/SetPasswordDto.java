package dev.aknb.ordersystem.user.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SetPasswordDto {

    @NotBlank(message = "TOKEN_NOT_BLANK")
    private String token;

    @NotBlank(message = "NEW_PASSWORD_NOT_BLANK")
    private String newPassword;
}
