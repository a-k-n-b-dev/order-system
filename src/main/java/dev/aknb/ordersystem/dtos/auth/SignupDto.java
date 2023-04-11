package dev.aknb.ordersystem.dtos.auth;

import dev.aknb.ordersystem.utils.validator.ValidPhoneNumber;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupDto {

    @NotBlank(message = "FULL_NAME_NOT_BLANK")
    @Size(min = 4, max = 100, message = "FULL_NAME_MIN4_MAX100")
    private String fullName;
    @NotBlank(message = "EMAIL_NOT_BLANK")
    @Email(message = "EMAIL_INVALID")
    private String email;
    @NotBlank(message = "PHONE_NUMBER_NOT_BLANK")
    @Size(min = 13, max = 15, message = "PHONE_NUMBER_MIN13_MAX15")
    @ValidPhoneNumber(message = "INVALID_PHONE_NUMBER")
    private String phoneNumber;
    @NotBlank(message = "PASSWORD_NOT_BLANK")
    @Size(min = 8, max = 64, message = "PASSWORD_MIN8_MAX64")
    private String password;
}
