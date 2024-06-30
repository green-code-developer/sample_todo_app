package jp.green_code.todo.entity;

import static jakarta.persistence.GenerationType.IDENTITY;
import static jp.green_code.todo.util.AppConstants.SYSTEM_ACCOUNT_ID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * アカウント
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "account")
@Entity
public class AccountEntity extends BaseEntity {

    @Id
    @SequenceGenerator(name = "account_account_id_seq")
    @GeneratedValue(strategy = IDENTITY)
    private Long accountId = SYSTEM_ACCOUNT_ID;
    private String accountStatus = "";
    private String name = "";
}
