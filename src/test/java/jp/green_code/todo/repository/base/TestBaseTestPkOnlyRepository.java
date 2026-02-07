package jp.green_code.todo.repository.base;

import java.lang.Long;
import jp.green_code.todo.entity.TestPkOnlyEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class TestBaseTestPkOnlyRepository {

    protected void test(BaseTestPkOnlyRepository repository) {
        var seed = getInitSeed();
        var data = generateTestData(seed);

        // insert(upsert)
        repository.upsert(data);

        // select 1回目
        var res = repository.findByPk(data.getColIntegerPk1(), data.getColIntegerPk2());
        assertTrue(res.isPresent());

        // insert 後の確認
        var stored = res.orElseThrow();
        assert4colIntegerPk1(data.getColIntegerPk1(), stored.getColIntegerPk1());
        assert4colIntegerPk2(data.getColIntegerPk2(), stored.getColIntegerPk2());

        // update(upsert)
        seed++;
        var data2 = generateTestData(seed);
        data2.setColIntegerPk1(data.getColIntegerPk1());
        data2.setColIntegerPk2(data.getColIntegerPk2());
        repository.upsert(data2);

        // select 2回目
        var res2 = repository.findByPk(data2.getColIntegerPk1(), data2.getColIntegerPk2());
        assertTrue(res2.isPresent());

        // update 後の確認
        var stored2 = res2.orElseThrow();
        assert4colIntegerPk1(data2.getColIntegerPk1(), stored2.getColIntegerPk1());
        assert4colIntegerPk2(data2.getColIntegerPk2(), stored2.getColIntegerPk2());

        // delete
        var deleteCount = repository.deleteByPk(data2.getColIntegerPk1(), data2.getColIntegerPk2());
        assertEquals(1, deleteCount);
        // select 3回目
        var stored3 = repository.findByPk(data2.getColIntegerPk1(), data2.getColIntegerPk2());
        assertTrue(stored3.isEmpty());
    }

    protected int getInitSeed() {
        return 1;
    }

    public TestPkOnlyEntity generateTestData(int seed) {
        var entity = new TestPkOnlyEntity();
        entity.setColIntegerPk1(generateTestData4colIntegerPk1(seed++));
        entity.setColIntegerPk2(generateTestData4colIntegerPk2(seed));
        return entity;
    }

    protected Long generateTestData4colIntegerPk1(int seed) {
        return (long) seed;
    }

    protected Long generateTestData4colIntegerPk2(int seed) {
        return (long) seed;
    }


    protected void assert4colIntegerPk1(Long expected, Long value) {
        assertEquals(expected, value);
    }

    protected void assert4colIntegerPk2(Long expected, Long value) {
        assertEquals(expected, value);
    }
}