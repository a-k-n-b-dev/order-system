package dev.aknb.ordersystem.repositories;

import dev.aknb.ordersystem.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u where u.email = ?1")
    Optional<User> findByEmail(String email);

    @Query("select (count(u) > 0) from User u where u.email = ?1 and u.verified = true")
    Boolean existsByEmail(String email);

    @Query("select u from User u where u.phoneNumber = ?1 and u.verified = false")
    Optional<User> findByPhoneNumberAndVerifiedIsFalse(String phoneNumber);

    @Query("select (count(u) > 0) from User u where u.phoneNumber = ?1 and u.verified = true")
    Boolean existsByPhoneNumber(String phoneNumber);

    @Query("select u from User u where u.approved = ?1 and u.verified = true")
    List<User> findAllByApproved(boolean approved);
}
