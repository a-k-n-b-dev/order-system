package dev.aknb.ordersystem.dtos.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefreshTokenDto {

    @NotBlank(message = "REFRESH_TOKEN_NOT_BLANK")
    private String refreshToken;
}
