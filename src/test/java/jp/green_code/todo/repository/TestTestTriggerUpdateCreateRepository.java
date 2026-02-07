package jp.green_code.todo.repository;

import jp.green_code.todo.repository.base.TestBaseTestTriggerUpdateCreateRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestTestTriggerUpdateCreateRepository extends TestBaseTestTriggerUpdateCreateRepository {

    @Autowired
    TestTriggerUpdateCreateRepository repository;

    @Test
    void test() {
        super.test(repository);
    }
}