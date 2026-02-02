package jp.green_code.todo.repository;

import org.springframework.stereotype.Repository;
import jp.green_code.todo.repository.base.RepositoryHelper;
import jp.green_code.todo.repository.base.BaseTestOnePkRepository;

/**
 * Table: test_one_pk
 */
@Repository
public class TestOnePkRepository extends BaseTestOnePkRepository {
    public TestOnePkRepository(RepositoryHelper helper) {
        super(helper);
    }
}