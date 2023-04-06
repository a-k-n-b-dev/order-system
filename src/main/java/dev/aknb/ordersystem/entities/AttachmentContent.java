package dev.aknb.ordersystem.entities;

import dev.aknb.ordersystem.entities.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "attachment_contents")
public class AttachmentContent extends BaseEntity {

    @Column(name = "attachment_url")
    private String attachmentUrl;

    @ManyToOne(optional = false)
    @JoinColumn(name = "attachment_id", referencedColumnName = "id", updatable = false, insertable = false)
    private Attachment attachment;

    @Column(name = "attachment_id")
    private Long attachmentId;
}
