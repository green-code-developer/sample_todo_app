package jp.green_code.todo.repository;

import java.util.Optional;
import jp.green_code.todo.entity.AccountEntity;
import org.springframework.data.repository.Repository;

public interface AccountJpaRepository extends Repository<AccountEntity, Long> {

  AccountEntity save(AccountEntity todo);

  Optional<AccountEntity> findById(long id);
}
