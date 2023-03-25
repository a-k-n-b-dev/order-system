package dev.aknb.ordersystem.user.auth;

import dev.aknb.ordersystem.exception.RestException;
import dev.aknb.ordersystem.mail.MailService;
import dev.aknb.ordersystem.message.MessageType;
import dev.aknb.ordersystem.role.Role;
import dev.aknb.ordersystem.role.RoleEnum;
import dev.aknb.ordersystem.user.User;
import dev.aknb.ordersystem.user.UserMapper;
import dev.aknb.ordersystem.user.UserRepository;
import dev.aknb.ordersystem.user.auth.dto.SignupDto;
import dev.aknb.ordersystem.verifyToken.VerifyTokenService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final VerifyTokenService verifyTokenService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final MailService mailService;
    private final UserMapper userMapper;

    public AuthService(VerifyTokenService verifyTokenService, PasswordEncoder passwordEncoder, UserRepository userRepository, MailService mailService, UserMapper userMapper) {
        this.verifyTokenService = verifyTokenService;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.mailService = mailService;
        this.userMapper = userMapper;
    }

    @Transactional
    public void signup(SignupDto signupDto) {

        Boolean exists = userRepository.existsByEmail(signupDto.getEmail());
        if (exists) {
            throw RestException.restThrow("Email", signupDto.getEmail(), MessageType.EMAIL_EXISTS.name());
        }

        exists = userRepository.existsByPhoneNumber(signupDto.getPhoneNumber());
        if (exists) {
            throw RestException.restThrow("Phone number", signupDto.getPhoneNumber(), MessageType.PHONE_NUMBER_EXISTS.name());
        }

        User user = userRepository
                .findByEmail(signupDto.getEmail())
                .orElse(new User());
        if (user.getUserRoles().isEmpty()) {
            user.getUserRoles().add(new Role(RoleEnum.WORKER));
        }
        userMapper.update(user, signupDto);
        user.setPassword(passwordEncoder.encode(signupDto.getPassword()));
        userRepository.save(user);
        mailService.sendVerifyToken(user.getEmail(), verifyTokenService.createToken(user));
    }
}
