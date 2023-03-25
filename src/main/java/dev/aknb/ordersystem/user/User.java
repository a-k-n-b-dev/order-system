package dev.aknb.ordersystem.user;

import dev.aknb.ordersystem.base.BaseEntity;
import dev.aknb.ordersystem.role.Role;
import jakarta.persistence.Table;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.*;

import java.time.Clock;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

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

    @Column(name = "verified")
    private Boolean verified = Boolean.FALSE;

    @Column(name = "password_changed_date")
    private Instant passwordChangedDate = Instant.now(Clock.systemUTC());

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = {
                    @JoinColumn(name = "user_id", referencedColumnName = "id"),
                    @JoinColumn(name = "full_name", referencedColumnName = "full_name")
            }, inverseJoinColumns = {
            @JoinColumn(name = "role_name", referencedColumnName = "name")})
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @BatchSize(size = 20)
    private Set<Role> userRoles = new HashSet<>();
}
