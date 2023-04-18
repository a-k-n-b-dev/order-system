package dev.aknb.ordersystem.services;

import dev.aknb.ordersystem.entities.User;
import dev.aknb.ordersystem.entities.VerifyToken;
import dev.aknb.ordersystem.models.MessageType;
import dev.aknb.ordersystem.models.RestException;
import dev.aknb.ordersystem.repositories.VerifyTokenRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class VerifyTokenService {
    private final VerifyTokenRepository verifyTokenRepository;

    public String createToken(User user) {
        VerifyToken verifyToken = verifyTokenRepository.findByUserId(user.getId())
                .orElse(new VerifyToken());
        if (verifyToken.isExpired()) {
            this.delete(verifyToken);
            verifyToken = new VerifyToken();
        }
        verifyToken.setToken(UUID.randomUUID().toString());
        verifyToken.setUserId(user.getId());
        return verifyTokenRepository.save(verifyToken).getToken();
    }

    public String getTokenIfSendingInOneMinuteOrThrowException(User user) {
        Optional<VerifyToken> optionalEntVerifyToken = verifyTokenRepository.findByUserId(user.getId());
        if (optionalEntVerifyToken.isPresent() &&
            !optionalEntVerifyToken.get().getLastModifiedDate().plusMinutes(1).isBefore(LocalDateTime.now())) {
            throw RestException.restThrow(HttpStatus.TOO_MANY_REQUESTS, MessageType.ONCE_PER_MINUTE.name());
        }
        return createToken(user);
    }


    public VerifyToken getIfValid(String token) {
        VerifyToken verifyToken = verifyTokenRepository.findByToken(token)
                .orElseThrow(() -> RestException.restThrow("token", token, MessageType.VERIFICATION_TOKEN_NOT_FOUND.name()));

        if (verifyToken.isExpired()) {
            this.delete(verifyToken);
            log.error("Verify code expired user mail: {}", verifyToken.getUser().getEmail());
            throw RestException.restThrow("token", token, MessageType.VERIFICATION_TOKEN_EXPIRED.name());
        }
        return verifyToken;
    }

    public void delete(VerifyToken verifyToken) {
        verifyTokenRepository.delete(verifyToken);
    }
} /* -> DEPRECATED CODES

    public String createCode(EntUser user) {
        EntVerifyToken entVerifyToken = verifyTokenRepository.findByUserId(user.getId())
                .orElse(new EntVerifyToken());
        entVerifyToken.setToken(getSixRandomNumber());
        entVerifyToken.setEmail(user.getEmail());
        entVerifyToken.setUserId(user.getId());
        return verifyTokenRepository.save(entVerifyToken).getToken();
    }
    protected String getSixRandomNumber() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
*/
