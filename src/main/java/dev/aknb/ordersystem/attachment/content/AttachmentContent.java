package dev.aknb.ordersystem.attachment.content;

import dev.aknb.ordersystem.attachment.Attachment;
import dev.aknb.ordersystem.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "attachment_contents")
public class AttachmentContent extends BaseEntity {

    @Column(name = "url")
    private String url;

    @ManyToOne(optional = false)
    @JoinColumn(name = "attachment_id", referencedColumnName = "id", updatable = false, insertable = false)
    private Attachment attachment;

    @Column(name = "attachment_id")
    private Long attachmentId;
}
