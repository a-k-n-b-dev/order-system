package dev.aknb.ordersystem.dtos.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ForgotPasswordDto {

    @NotBlank(message = "EMAIL_NOT_BLANK")
    private String email;
}
