package jp.green_code.todo.repository;

import org.springframework.stereotype.Repository;
import jp.green_code.todo.repository.base.RepositoryHelper;
import jp.green_code.todo.repository.base.BaseTestTriggerUpdateCreateRepository;

/**
 * Table: test_trigger_update_create
 */
@Repository
public class TestTriggerUpdateCreateRepository extends BaseTestTriggerUpdateCreateRepository {
    public TestTriggerUpdateCreateRepository(RepositoryHelper helper) {
        super(helper);
    }
}