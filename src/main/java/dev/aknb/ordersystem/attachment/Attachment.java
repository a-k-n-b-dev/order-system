package dev.aknb.ordersystem.attachment;

import dev.aknb.ordersystem.attachment.content.AttachmentContent;
import dev.aknb.ordersystem.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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
    @OneToMany(mappedBy = "attachment")
    private List<AttachmentContent> attachmentContents;
}
