package dev.aknb.ordersystem.user.auth.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDto {

    @NotBlank(message = "EMAIL_NOT_BLANK")
    private String email;

    @NotBlank(message = "PASSWORD_NOT_BLANK")
    @Size(min = 8, max = 64, message = "PASSWORD_MIN8_MAX64")
    private String password;
}
