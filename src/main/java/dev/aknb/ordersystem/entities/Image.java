package dev.aknb.ordersystem.entities;

import dev.aknb.ordersystem.entities.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Getter
@Setter
@Entity
@Table(name = "images")
@Where(clause = "deleted = false")
@SQLDelete(sql = "update images set deleted = true where id = ?", check = ResultCheckStyle.COUNT)
public class Image extends BaseEntity {

    @Column(name = "url")
    private String url;
    @Column(name = "original_filename")
    private String originalFilename;
    @Column(name = "unique_name")
    private String uniqueName;
    @Column(name = "token")
    private String token;
    @Column(name = "extension")
    private String extension;
    @Column(name = "size")
    private Long size;

    @ManyToOne(optional = false)
    @JoinColumn(name = "order_id", referencedColumnName = "id", updatable = false, insertable = false)
    private Order order;

    @Column(name = "order_id")
    private Long orderId;
}
