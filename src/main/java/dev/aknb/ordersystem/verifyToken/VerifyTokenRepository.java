package dev.aknb.ordersystem.verifyToken;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerifyTokenRepository extends JpaRepository<VerifyToken, Long> {

    @Query("SELECT vt FROM VerifyToken vt WHERE vt.token = ?1")
    Optional<VerifyToken> findByToken(String token);

    @Query("SELECT vt FROM VerifyToken vt WHERE vt.userId = ?1")
    Optional<VerifyToken> findByUserId(Long userId);
}
