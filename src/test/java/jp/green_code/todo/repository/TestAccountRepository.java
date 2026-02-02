package jp.green_code.todo.repository;

import jp.green_code.todo.repository.base.TestBaseAccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestAccountRepository extends TestBaseAccountRepository {

    @Autowired
    AccountRepository repository;

    @Test
    void test() {
        super.test(repository);
    }
}