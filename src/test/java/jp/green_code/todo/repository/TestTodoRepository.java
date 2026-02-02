package jp.green_code.todo.repository;

import jp.green_code.todo.repository.base.TestBaseTodoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestTodoRepository extends TestBaseTodoRepository {

    @Autowired
    TodoRepository repository;

    @Test
    void test() {
        super.test(repository);
    }

    @Override
    protected Long generateTestData4updatedBy(int seed) {
        return -1L;
    }

    @Override
    protected Long generateTestData4createdBy(int seed) {
        return -1L;
    }
}