package jp.green_code.todo.repository;

import org.springframework.stereotype.Repository;
import jp.green_code.todo.repository.base.RepositoryHelper;
import jp.green_code.todo.repository.base.BaseTestNoPkRepository;

/**
 * Table: test_no_pk
 */
@Repository
public class TestNoPkRepository extends BaseTestNoPkRepository {
    public TestNoPkRepository(RepositoryHelper helper) {
        super(helper);
    }
}