package dev.aknb.ordersystem.entities;

import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import dev.aknb.ordersystem.entities.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "customers")
@Where(clause = "deleted = false")
@SQLDelete(sql = "UPDATE customers SET deleted = TRUE WHERE id = ?", check = ResultCheckStyle.COUNT)
public class Customer extends BaseEntity{

    @Column(name = "full_name", length = 100)
    private String fullName;

    @Column(name = "phone_number", length = 15)
    private String phoneNumber;

    @Column(name = "address", columnDefinition = "text")
    private String address;
}
