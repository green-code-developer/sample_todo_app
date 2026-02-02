package jp.green_code.todo.repository;

import org.springframework.stereotype.Repository;
import jp.green_code.todo.repository.base.RepositoryHelper;
import jp.green_code.todo.repository.base.BaseTestAllWithDefaultRepository;

/**
 * Table: test_all_with_default
 */
@Repository
public class TestAllWithDefaultRepository extends BaseTestAllWithDefaultRepository {
    public TestAllWithDefaultRepository(RepositoryHelper helper) {
        super(helper);
    }
}