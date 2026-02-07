package jp.green_code.todo.repository;

import org.springframework.stereotype.Repository;
import jp.green_code.todo.repository.base.RepositoryHelper;
import jp.green_code.todo.repository.base.BaseTestPkOnlyRepository;

/**
 * Table: test_pk_only
 */
@Repository
public class TestPkOnlyRepository extends BaseTestPkOnlyRepository {
    public TestPkOnlyRepository(RepositoryHelper helper) {
        super(helper);
    }
}