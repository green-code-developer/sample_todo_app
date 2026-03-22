package jp.green_code.todo.domain.repository;

import jp.green_code.todo.domain.repository.base.TestBaseAccountRepository;
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