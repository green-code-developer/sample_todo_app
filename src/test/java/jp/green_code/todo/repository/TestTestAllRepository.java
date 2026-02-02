package jp.green_code.todo.repository;

import jp.green_code.todo.repository.base.TestBaseTestAllRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestTestAllRepository extends TestBaseTestAllRepository {

    @Autowired
    TestAllRepository repository;

    @Test
    void test() {
        super.test(repository);
    }
}