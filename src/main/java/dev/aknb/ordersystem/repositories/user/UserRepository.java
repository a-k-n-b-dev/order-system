package dev.aknb.ordersystem.repositories.user;

import dev.aknb.ordersystem.entities.User;
import dev.aknb.ordersystem.models.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u where lower(u.email) = lower(?1)")
    Optional<User> findByEmail(String email);

    @Query("select (count(u) > 0) from User u where u.email = ?1 and u.status = ?2")
    Boolean existsByEmailAndStatus(String email, UserStatus status);

    @Query("select (count(u) > 0) from User u where u.phoneNumber = ?1 and u.status = ?2")
    Boolean existsByPhoneNumberAndStatus(String phoneNumber, UserStatus status);






    @Query("select u from User u where u.phoneNumber = ?1 and u.status = ?2")
    Optional<User> findByPhoneNumberAndStatus(String phoneNumber, UserStatus status);

    @Query("select u from User u where u.phoneNumber = ?1")
    Optional<User> findByPhoneNumber(String phoneNumber);
}
