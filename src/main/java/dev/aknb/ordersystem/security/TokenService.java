package dev.aknb.ordersystem.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TokenService {
    public static final String BEARER_TOKEN = "Bearer ";
    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;

    public TokenService(JwtEncoder jwtEncoder, JwtDecoder jwtDecoder) {

        this.jwtEncoder = jwtEncoder;
        this.jwtDecoder = jwtDecoder;
    }

    public String generateToken(String subject) {
        return generateToken(new HashMap<>(), subject, 1440L);
    }

    public String generateToken(String subject, Long minutesToExpire) {
        return generateToken(new HashMap<>(), subject, minutesToExpire);
    }

    public String generateToken(String subject, Collection<? extends GrantedAuthority> securityRoles) {
        return generateToken(subject, securityRoles, 1440L);
    }

    public String generateToken(String subject, Collection<? extends GrantedAuthority> securityRoles, Long minutesToExpire) {

        String authoritiesScope = securityRoles
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        Map<String, Object> claims = new HashMap<>();
        claims.put("scope", authoritiesScope);
        return generateToken(claims, subject, minutesToExpire);
    }

    public String generateToken(Map<String, Object> extraClaims, String subject, Long minutesToExpire) {

        Instant now = Instant.now(Clock.systemUTC());

        JwtClaimsSet claims = JwtClaimsSet
                .builder()
                .issuer("self")
                .issuedAt(now)
                .claims(stringObjectMap -> stringObjectMap.putAll(extraClaims))
                .expiresAt(now.plus(minutesToExpire, ChronoUnit.MINUTES))
                .subject(subject).build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public boolean validateToken(String token) {
        try {
            this.jwtDecoder.decode(token);
            return true;
        } catch (JwtException e) {
            log.info("Invalid JWT signature.");
            log.trace("Invalid JWT signature trace: ", e);
            return false;
        }
    }

    public Optional<Authentication> getAuthentication(String token) {

        if (validateToken(token)) {

            Jwt claims = this.extractAllClaims(token);
            Collection<GrantedAuthority> authorities =
                    claims.hasClaim("scope") ?
                            Arrays.stream(
                                    claims.getClaimAsString("scope").split(",")
                            ).map(SimpleGrantedAuthority::new).collect(Collectors.toList()) :
                            Collections.emptyList();
            User user = new User(claims.getSubject(), "", authorities);
            return Optional.of(new UsernamePasswordAuthenticationToken(user, token, authorities));
        }
        return Optional.empty();
    }

    public Boolean isIssuedAtAfter(String token, Instant date) {
        Jwt jwt = extractAllClaims(token);
        return Objects.requireNonNull(jwt.getIssuedAt()).isAfter(date);
    }

    public String extractSubject(String token) {

        return extractClaim(token, Jwt::getSubject);
    }

    public <T> T extractClaim(String token, Function<Jwt, T> claimsResolver) {

        final Jwt claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Jwt extractAllClaims(String token) {

        return this.jwtDecoder.decode(token);
    }

    public Boolean hasBearerToken(String bearerToken) {

        return bearerToken != null &&
               bearerToken.startsWith(TokenService.BEARER_TOKEN) &&
               !bearerToken.equalsIgnoreCase(TokenService.BEARER_TOKEN);
    }

    public String getTokenOrElseNull(String bearerToken) {

        if (!bearerToken.equalsIgnoreCase(TokenService.BEARER_TOKEN)) {
            return bearerToken.replace("Bearer ", "");
        }
        return null;
    }
}
