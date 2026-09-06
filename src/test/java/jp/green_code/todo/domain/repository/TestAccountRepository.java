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

    /**
     * created_by はトリガーが更新前の値へ戻すため、seed で値を変えると
     * update 後の比較が一致しない。テストデータでは固定値を使う
     */
    @Override
    protected long generateTestData4createdBy(int seed) {
        return -1L;
    }
}