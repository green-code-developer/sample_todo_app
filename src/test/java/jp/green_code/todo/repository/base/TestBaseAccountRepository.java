package jp.green_code.todo.repository.base;

import java.lang.Long;
import java.lang.String;
import java.time.OffsetDateTime;
import jp.green_code.todo.entity.AccountEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class TestBaseAccountRepository {

    protected void test(BaseAccountRepository repository) {
        var seed = getInitSeed();
        var data = generateTestData(seed);

        // insert(upsert)
        data.setAccountId(null);
        var id = repository.upsert(data);

        // select 1回目
        var res = repository.findByPk(id);
        data.setAccountId(id);
        assertTrue(res.isPresent());

        // insert 後の確認
        var stored = res.orElseThrow();
        assert4accountId(data.getAccountId(), stored.getAccountId());
        assert4accountStatus(data.getAccountStatus(), stored.getAccountStatus());
        assert4name(data.getName(), stored.getName());
        // updated_at はnow() を設定するカラムのためassertしない
        assert4updatedBy(data.getUpdatedBy(), stored.getUpdatedBy());
        // created_at はnow() を設定するカラムのためassertしない
        assert4createdBy(data.getCreatedBy(), stored.getCreatedBy());

        // update(upsert)
        seed++;
        var data2 = generateTestData(seed);
        data2.setAccountId(id);
        repository.upsert(data2);

        // select 2回目
        var res2 = repository.findByPk(id);
        assertTrue(res2.isPresent());

        // update 後の確認
        var stored2 = res2.orElseThrow();
        assert4accountId(data2.getAccountId(), stored2.getAccountId());
        assert4accountStatus(data2.getAccountStatus(), stored2.getAccountStatus());
        assert4name(data2.getName(), stored2.getName());
        // updated_at はnow() を設定するカラムのためassertしない
        assert4updatedBy(data2.getUpdatedBy(), stored2.getUpdatedBy());
        // created_at はupdate 対象外のため変更前と変わらないことを確認
        assert4createdAt(stored.getCreatedAt(), stored2.getCreatedAt());
        // created_by はupdate 対象外のため変更前と変わらないことを確認
        assert4createdBy(stored.getCreatedBy(), stored2.getCreatedBy());

        // delete
        var deleteCount = repository.deleteByPk(id);
        assertEquals(1, deleteCount);
        // select 3回目
        var stored3 = repository.findByPk(id);
        assertTrue(stored3.isEmpty());
    }

    protected int getInitSeed() {
        return 1;
    }

    public AccountEntity generateTestData(int seed) {
        var entity = new AccountEntity();
        entity.setAccountId(generateTestData4accountId(seed++));
        entity.setAccountStatus(generateTestData4accountStatus(seed++));
        entity.setName(generateTestData4name(seed++));
        entity.setUpdatedAt(generateTestData4updatedAt(seed++));
        entity.setUpdatedBy(generateTestData4updatedBy(seed++));
        entity.setCreatedAt(generateTestData4createdAt(seed++));
        entity.setCreatedBy(generateTestData4createdBy(seed));
        return entity;
    }

    protected Long generateTestData4accountId(int seed) {
        return (long) seed;
    }

    protected String generateTestData4accountStatus(int seed) {
        return seed + "";
    }

    protected String generateTestData4name(int seed) {
        return seed + "";
    }

    protected OffsetDateTime generateTestData4updatedAt(int seed) {
        return OffsetDateTime.of(2001, 1, 1, 0, 0, 0, 0, java.time.ZoneOffset.UTC).plusMinutes(seed);
    }

    protected Long generateTestData4updatedBy(int seed) {
        return (long) seed;
    }

    protected OffsetDateTime generateTestData4createdAt(int seed) {
        return OffsetDateTime.of(2001, 1, 1, 0, 0, 0, 0, java.time.ZoneOffset.UTC).plusMinutes(seed);
    }

    protected Long generateTestData4createdBy(int seed) {
        return (long) seed;
    }


    protected void assert4accountId(Long expected, Long value) {
        assertEquals(expected, value);
    }

    protected void assert4accountStatus(String expected, String value) {
        assertEquals(expected, value.trim());
    }

    protected void assert4name(String expected, String value) {
        assertEquals(expected, value.trim());
    }

    protected void assert4updatedAt(OffsetDateTime expected, OffsetDateTime value) {
        assertEquals(expected, value);
    }

    protected void assert4updatedBy(Long expected, Long value) {
        assertEquals(expected, value);
    }

    protected void assert4createdAt(OffsetDateTime expected, OffsetDateTime value) {
        assertEquals(expected, value);
    }

    protected void assert4createdBy(Long expected, Long value) {
        assertEquals(expected, value);
    }
}