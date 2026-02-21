package jp.green_code.todo.repository;

import jp.green_code.todo.repository.base.BaseAccountRepository;
import org.springframework.stereotype.Repository;

/**
 * Table: account
 */
@Repository
public class AccountRepository extends BaseAccountRepository {
    public AccountRepository(RepositoryHelper helper) {
        super(helper);
    }
}