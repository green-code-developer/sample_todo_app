package jp.green_code.todo.entity;

import static jp.green_code.todo.util.AppConstants.SYSTEM_ACCOUNT_ID;
import static jp.green_code.todo.util.ThreadLocalUtil.getAccountId;
import static org.hibernate.annotations.SourceType.DB;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.io.Serializable;
import java.time.OffsetDateTime;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * エンティティ共通クラス
 */
@Data
@MappedSuperclass
public class BaseEntity implements Serializable {

    @UpdateTimestamp(source = DB)
    private OffsetDateTime updatedAt;

    private Long updatedBy = SYSTEM_ACCOUNT_ID;

    @CreationTimestamp(source = DB)
    @Column(updatable = false)
    private OffsetDateTime createdAt;

    @Column(updatable = false)
    private Long createdBy = SYSTEM_ACCOUNT_ID;
}
