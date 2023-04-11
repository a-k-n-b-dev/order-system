package dev.aknb.ordersystem.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@ConfigurationProperties("rsa")
public record RsaKeyConfig(RSAPublicKey publicKey, RSAPrivateKey privateKey) {
}
