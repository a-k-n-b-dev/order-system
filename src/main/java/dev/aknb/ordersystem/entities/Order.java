package dev.aknb.ordersystem.entities;

import dev.aknb.ordersystem.entities.base.BaseEntity;
import dev.aknb.ordersystem.models.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
@Where(clause = "deleted = false")
@SQLDelete(sql = "UPDATE orders SET deleted = TRUE WHERE id = ?", check = ResultCheckStyle.COUNT)
public class Order extends BaseEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "accepted_date")
    private LocalDate acceptedDate;

    @Column(name = "delivered_date")
    private LocalDate deliveredDate;

    @Column(name = "size")
    private String size;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "agreement")
    private String agreement;

    @Column(name = "materials")
    private String materials;

    @Column(name = "price")
    private String price;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderStatus status;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "id", updatable = false, insertable = false)
    private User user;

    @Column(name = "user_id")
    private Long userId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "customer_id", referencedColumnName = "id", updatable = false, insertable = false)
    private Customer customer;

    @Column(name = "customer_id")
    private Long customerId;

    @OneToMany(mappedBy = "order")
    private List<Image> images = new ArrayList<>();
}
