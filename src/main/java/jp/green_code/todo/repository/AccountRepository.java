package jp.green_code.todo.repository;

import org.springframework.stereotype.Repository;
import jp.green_code.todo.repository.base.RepositoryHelper;
import jp.green_code.todo.repository.base.BaseAccountRepository;

/**
 * Table: account
 */
@Repository
public class AccountRepository extends BaseAccountRepository {
    public AccountRepository(RepositoryHelper helper) {
        super(helper);
    }
}