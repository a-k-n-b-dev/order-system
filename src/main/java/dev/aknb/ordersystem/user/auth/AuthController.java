package dev.aknb.ordersystem.user.auth;

import dev.aknb.ordersystem.exception.RestException;
import dev.aknb.ordersystem.message.MessageType;
import dev.aknb.ordersystem.project.ApiConstants;
import dev.aknb.ordersystem.project.ProjectConfig;
import dev.aknb.ordersystem.response.Response;
import dev.aknb.ordersystem.security.SecurityContextUtils;
import dev.aknb.ordersystem.user.UserDto;
import dev.aknb.ordersystem.user.auth.dto.ChangePasswordDto;
import dev.aknb.ordersystem.user.auth.dto.LoginDto;
import dev.aknb.ordersystem.user.auth.dto.SignupDto;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(ApiConstants.API_AUTH)
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<Response<String>> signup(@Valid @RequestBody SignupDto signupDto) {

        log.info("Rest request to signup phone number: {}", signupDto.getPhoneNumber());
        return ResponseEntity.ok(
                Response.ok(authService.signup(signupDto)));
    }

    @GetMapping("/signup/verify-mail")
    public ResponseEntity<Response<TokenDataDto<UserDto>>> verifyMail(@RequestParam("token") String token) {

        log.info("Rest request to verify mail token: {}", token);
        return ResponseEntity.ok(
                Response.ok(authService.verifyMail(token)));
    }

    @GetMapping("/signup/verify-email/resend/{token}")
    public ResponseEntity<Response<String>> resendLink(@PathVariable("token") String jwtToken) {

        log.info("Rest request to resend verify-mail");
        return ResponseEntity.ok(
                Response.ok(authService.resendVerifyMailLink(jwtToken)));
    }

    @PostMapping("/login")
    public ResponseEntity<Response<TokenDataDto<UserDto>>> login(@RequestBody @Valid LoginDto req) {

        log.info("Login req email: {}", req.getEmail());
        return ResponseEntity.ok(
                Response.ok(authService.login(req)));
    }

    @SecurityRequirement(name = ProjectConfig.NAME)
    @ApiResponse(responseCode = "401", description = "A jwt token must be provided in the authentication header")
    @PatchMapping("/password/change")
    public ResponseEntity<Response<String>> changePassword(@RequestBody ChangePasswordDto request) {

        String email = SecurityContextUtils.getUserEmail().orElseThrow(() ->
                RestException.restThrow(HttpStatus.BAD_REQUEST, MessageType.ERROR.name()));
        log.info("Reset password request for email: {}", email);
        authService.changePassword(request, email);
        return ResponseEntity.ok(
                Response.ok("Password successfully changed"));
    }

    @GetMapping("/password/reset")
    public ResponseEntity<Response<String>> resetPassword(@RequestParam("email") String email) {

        log.info("Reset password request for email: {}", email);
        authService.resetPassword(email);
        return ResponseEntity.ok(
                Response.ok("We sent reset password link to your email. Please check your email"));
    }

    @GetMapping("/validate")
    public ResponseEntity<Response<String>> checkToken(@RequestParam("token") String token) {

        log.info("REST request to validate token");
        token = authService.validateToken(token);
        return ResponseEntity.ok(
                Response.ok(token));
    }

    @SecurityRequirement(name = ProjectConfig.NAME)
    @ApiResponse(responseCode = "401", description = "A jwt token must be provided in the authentication header")
    @PatchMapping("/password/set")
    public ResponseEntity<Response<String>> setPassword(@RequestParam String newPassword) {

        String email = SecurityContextUtils.getUserEmail().orElseThrow(() ->
                RestException.restThrow(HttpStatus.BAD_REQUEST, MessageType.ERROR.name()));
        log.info("Rest request to set new password to user email: {}", email);
        authService.setPassword(newPassword, email);
        return ResponseEntity.ok(
                Response.ok("Password successfully set!"));
    }
}
