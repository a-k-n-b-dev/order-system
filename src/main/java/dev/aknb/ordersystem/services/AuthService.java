package dev.aknb.ordersystem.services;

import dev.aknb.ordersystem.dtos.auth.ChangePasswordDto;
import dev.aknb.ordersystem.dtos.auth.LoginDto;
import dev.aknb.ordersystem.dtos.auth.SignupDto;
import dev.aknb.ordersystem.dtos.auth.TokenDataDto;
import dev.aknb.ordersystem.dtos.security.SecurityUser;
import dev.aknb.ordersystem.dtos.user.UserDto;
import dev.aknb.ordersystem.entities.Role;
import dev.aknb.ordersystem.entities.User;
import dev.aknb.ordersystem.entities.VerifyToken;
import dev.aknb.ordersystem.mappers.UserMapper;
import dev.aknb.ordersystem.models.MessageType;
import dev.aknb.ordersystem.models.RestException;
import dev.aknb.ordersystem.models.RoleEnum;
import dev.aknb.ordersystem.models.UserStatus;
import dev.aknb.ordersystem.repositories.user.UserRepository;
import jakarta.annotation.Nonnull;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.util.Optional;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final VerifyTokenService verifyTokenService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final MailService mailService;
    private final UserMapper userMapper;

    public AuthService(AuthenticationManager authenticationManager, VerifyTokenService verifyTokenService, PasswordEncoder passwordEncoder, UserRepository userRepository, TokenService tokenService, MailService mailService, UserMapper userMapper) {
        this.authenticationManager = authenticationManager;
        this.verifyTokenService = verifyTokenService;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.tokenService = tokenService;
        this.mailService = mailService;
        this.userMapper = userMapper;
    }

    @Transactional
    public String signup(SignupDto signupDto) {

        Boolean exists = userRepository.existsByEmailAndStatus(signupDto.getEmail(), UserStatus.APPROVED);
        if (exists) {
            throw RestException.restThrow("Email", signupDto.getEmail(), MessageType.EMAIL_EXISTS.name());
        }

        exists = userRepository.existsByPhoneNumberAndStatus(signupDto.getPhoneNumber(), UserStatus.APPROVED);
        if (exists) {
            throw RestException.restThrow("Phone number", signupDto.getPhoneNumber(), MessageType.PHONE_NUMBER_EXISTS.name());
        }

        User user = userRepository.findByEmail(signupDto.getEmail()).orElse(new User());
        if (user.getRole() == null) {
            user.setRole(new Role(RoleEnum.ADMIN));
        }
        userMapper.update(user, signupDto);
        user.setPassword(passwordEncoder.encode(signupDto.getPassword()));
        userRepository.save(user);
        String token = tokenService.generateToken(user.getEmail());
        mailService.sendVerifyToken(user.getEmail(), verifyTokenService.createToken(user));
        return token;
    }

    public void verifyMail(String token) {

        VerifyToken verifyToken = verifyTokenService.getIfValid(token);
        User user = verifyToken.getUser();
        user.setStatus(UserStatus.VERIFIED);
        userRepository.save(user);
        verifyTokenService.delete(verifyToken);
        mailService.sendApprove(user.getEmail(), verifyTokenService.createToken(user));
    }

    public void approveUser(String token) {

        VerifyToken verifyToken = verifyTokenService.getIfValid(token);
        User user = verifyToken.getUser();
        if (user.getStatus().equals(UserStatus.PENDING)) {
            throw RestException.restThrow(HttpStatus.BAD_REQUEST, MessageType.EMAIL_NOT_VERIFIED.name());
        }
        user.setStatus(UserStatus.APPROVED);
        userRepository.save(user);
        verifyTokenService.delete(verifyToken);
        mailService.sendApproved(user.getEmail(), user.getFullName());
    }

    public String resendVerifyMailLink(String jwtToken) {

        String email = tokenService.extractSubject(jwtToken);
        User user = getOrElseThrow(email);
        if (user.getStatus().equals(UserStatus.VERIFIED)) {
            throw RestException.restThrow(HttpStatus.BAD_REQUEST, MessageType.ALREADY_VERIFIED.name());
        }
        mailService.sendVerifyToken(
                email,
                verifyTokenService.getTokenIfSendingInOneMinuteOrThrowException(user));
        return jwtToken;
    }

    public TokenDataDto<UserDto> login(@Nonnull LoginDto request) {

        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        Optional.ofNullable(authenticate)
                .map(Authentication::getPrincipal)
                .orElseThrow(() -> RestException.restThrow(HttpStatus.UNAUTHORIZED, MessageType.INVALID_CREDENTIALS.name()));

        SecurityContextHolder.getContext().setAuthentication(authenticate);
        SecurityUser securityUser = (SecurityUser) authenticate.getPrincipal();

        if (!securityUser.isEnabled()) {
            throw RestException.restThrow(HttpStatus.UNAUTHORIZED, MessageType.EMAIL_NOT_VERIFIED.name());
        }
        String token = tokenService.generateToken(securityUser.getEmail(), securityUser.getAuthorities());
        return new TokenDataDto<>(userMapper.toUserDto(securityUser), token);
    }

    public void changePassword(ChangePasswordDto request, String email) {

        User user = getOrElseThrow(email);
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw RestException.restThrow(HttpStatus.BAD_REQUEST, MessageType.PASSWORD_NOT_MATCHED.name());
        }
        user.setPassword(request.getNewPassword());
        user.setPasswordChangedDate(Instant.now(Clock.systemUTC()));
        userRepository.save(user);
    }

    public void resetPassword(String email) {

        User user = getOrElseThrow(email);
        mailService.sendResetPassword(email, verifyTokenService.createToken(user));
    }

    public String validateToken(String token) {

        VerifyToken validToken = verifyTokenService.getIfValid(token);
        User user = validToken.getUser();
        token = tokenService.generateToken(user.getEmail(), 15L);
        return token;
    }

    public void setPassword(String newPassword, String email) {

        User user = getOrElseThrow(email);
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setPasswordChangedDate(Instant.now(Clock.systemUTC()));
        userRepository.save(user);
    }

    private User getOrElseThrow(String email) {
        return userRepository.findByEmail(email).orElseThrow(() ->
                RestException.restThrow(HttpStatus.NOT_FOUND, MessageType.USER_NOT_FOUND_EMAIL.name(), email));
    }
}
