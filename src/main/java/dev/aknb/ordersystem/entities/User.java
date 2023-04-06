package dev.aknb.ordersystem.entities;

import dev.aknb.ordersystem.entities.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.Clock;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "users")
@Where(clause = "deleted = false")
@SQLDelete(sql = "UPDATE users SET deleted = TRUE WHERE id = ?", check = ResultCheckStyle.COUNT)
public class User extends BaseEntity {

    @Column(name = "full_name", length = 100)
    private String fullName;

    @Column(name = "email")
    private String email;

    @Column(name = "password", length = 64)
    private String password;

    @Column(name = "phone_number", length = 15)
    private String phoneNumber;

    @Column(name = "address", columnDefinition = "text")
    private String address;

    @Column(name = "verified")
    private Boolean verified = Boolean.FALSE;

    @Column(name = "approved")
    private Boolean approved = Boolean.FALSE;

    @Column(name = "password_changed_date")
    private Instant passwordChangedDate = Instant.now(Clock.systemUTC());

    @ManyToOne
    @JoinColumn(name = "role_name", referencedColumnName = "name")
    private Role role;
}
