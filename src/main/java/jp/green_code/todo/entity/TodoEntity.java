package jp.green_code.todo.entity;

import static jakarta.persistence.GenerationType.IDENTITY;
import static jp.green_code.todo.util.AppConstants.SYSTEM_ACCOUNT_ID;
import static jp.green_code.todo.util.DateUtil.BLANK_OFFSET_TIME;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * やること
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "todo")
@Entity
public class TodoEntity extends BaseEntity {

    @Id
    @SequenceGenerator(name = "todo_todo_id_seq")
    @GeneratedValue(strategy = IDENTITY)
    private Long todoId = SYSTEM_ACCOUNT_ID;

    @Column(insertable = false)
    private String todoStatus = "";

    private String detail = "";

    private OffsetDateTime deadline = BLANK_OFFSET_TIME;
}
