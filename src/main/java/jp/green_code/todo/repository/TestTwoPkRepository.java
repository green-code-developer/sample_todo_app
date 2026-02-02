package jp.green_code.todo.repository;

import org.springframework.stereotype.Repository;
import jp.green_code.todo.repository.base.RepositoryHelper;
import jp.green_code.todo.repository.base.BaseTestTwoPkRepository;

/**
 * Table: test_two_pk
 */
@Repository
public class TestTwoPkRepository extends BaseTestTwoPkRepository {
    public TestTwoPkRepository(RepositoryHelper helper) {
        super(helper);
    }
}