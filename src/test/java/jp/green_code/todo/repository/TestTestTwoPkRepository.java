package jp.green_code.todo.repository;

import jp.green_code.todo.repository.base.TestBaseTestTwoPkRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestTestTwoPkRepository extends TestBaseTestTwoPkRepository {

    @Autowired
    TestTwoPkRepository repository;

    @Test
    void test() {
        super.test(repository);
    }
}