package dev.aknb.ordersystem.entities;

import dev.aknb.ordersystem.models.AttachmentStatus;
import dev.aknb.ordersystem.entities.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "attachments")
@Where(clause = "deleted = false")
@SQLDelete(sql = "update attachments set deleted = true where id = ?", check = ResultCheckStyle.COUNT)
public class Attachment extends BaseEntity {

    @Column(name = "name")
    private String name;
    @Column(name = "filename")
    private String filename;
    @Column(name = "file_size")
    private Long fileSize;
    @Column(name = "content_type")
    private String contentType;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private AttachmentStatus status;
    @OneToMany(mappedBy = "attachment")
    private List<AttachmentContent> attachmentContents;
}
