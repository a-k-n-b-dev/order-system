package dev.aknb.ordersystem.user.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ForgotPasswordDto {

    @NotBlank(message = "EMAIL_NOT_BLANK")
    private String email;
}
