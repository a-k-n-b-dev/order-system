package dev.aknb.ordersystem.role;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;


@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "roles")
public class Role implements Serializable {

    @Id
    @Column(name = "name")
    @Enumerated(EnumType.STRING)
    private RoleEnum name;
}
