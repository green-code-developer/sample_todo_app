package jp.green_code.todo.repository;

import org.springframework.stereotype.Repository;
import jp.green_code.todo.repository.base.RepositoryHelper;
import jp.green_code.todo.repository.base.BaseTestAllRepository;

/**
 * Table: test_all
 */
@Repository
public class TestAllRepository extends BaseTestAllRepository {
    public TestAllRepository(RepositoryHelper helper) {
        super(helper);
    }
}