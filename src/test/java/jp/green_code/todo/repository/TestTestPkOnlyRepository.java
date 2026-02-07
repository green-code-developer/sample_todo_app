package jp.green_code.todo.repository;

import jp.green_code.todo.repository.base.TestBaseTestPkOnlyRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestTestPkOnlyRepository extends TestBaseTestPkOnlyRepository {

    @Autowired
    TestPkOnlyRepository repository;

    @Test
    void test() {
        super.test(repository);
    }
}