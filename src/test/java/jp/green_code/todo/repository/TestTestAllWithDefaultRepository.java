package jp.green_code.todo.repository;

import jp.green_code.todo.entity.TestAllWithDefaultEntity;
import jp.green_code.todo.repository.base.TestBaseTestAllWithDefaultRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class TestTestAllWithDefaultRepository extends TestBaseTestAllWithDefaultRepository {

    @Autowired
    TestAllWithDefaultRepository repository;

    @Test
    void test() {
        super.test(repository);
    }

    @Test
    void testOmitted() {
        // 全てのカラムに初期値とnot null が付いているので、全てがnull のentity でもinsert できるはず
        var allNullEntity = new TestAllWithDefaultEntity();

        // insert(upsert)
        var id = repository.upsert(allNullEntity);

        // select
        var stored = repository.findByPk(id);
        assertTrue(stored.isPresent());

        // delete
        var deleteCount = repository.deleteByPk(id);
        assertEquals(1, deleteCount);

        // select
        var stored3 = repository.findByPk(id);
        assertTrue(stored3.isEmpty());
    }


    @Test
    void testOnlyOneColumn() {
        // 1カラムだけ値を入れる
        var entity = new TestAllWithDefaultEntity();
        entity.setColText("testOnlyOneColumn()");

        // insert(upsert)
        var id = repository.upsert(entity);

        // select
        var stored = repository.findByPk(id);
        assertTrue(stored.isPresent());

        // delete
        var deleteCount = repository.deleteByPk(id);
        assertEquals(1, deleteCount);

        // select
        var stored3 = repository.findByPk(id);
        assertTrue(stored3.isEmpty());
    }
}