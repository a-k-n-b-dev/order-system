package dev.aknb.ordersystem.user.auth;
import dev.aknb.ordersystem.project.ApiConstants;
import dev.aknb.ordersystem.response.Response;
import dev.aknb.ordersystem.user.auth.dto.SignupDto;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(ApiConstants.API_AUTH)
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<Response<?>> signup(@Valid @RequestBody SignupDto signupDto) {

        log.info("Rest request to signup phone number: {}", signupDto.getPhoneNumber());
        authService.signup(signupDto);
        return ResponseEntity.ok(Response.ok());
    }
}
