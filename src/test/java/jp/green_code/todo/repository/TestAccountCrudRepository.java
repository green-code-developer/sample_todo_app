package jp.green_code.todo.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import jp.green_code.todo.TestSupportService;
import jp.green_code.todo.entity.AccountEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestAccountCrudRepository {

  private final TestSupportService testSupportService;
  private final AccountCrudRepository repository;

  @Autowired
  public TestAccountCrudRepository(
      TestSupportService testSupportService, AccountCrudRepository repository) {
    this.testSupportService = testSupportService;
    this.repository = repository;
  }

  @AfterEach
  void after() {
    testSupportService.clean();
  }

  @Test
  void crud() {
    var createdBy = TestSupportService.TEST_ACCOUNT_ID_1;
    var updatedBy = TestSupportService.TEST_ACCOUNT_ID_2;
    var salt = 0;

    // 初期データ
    var insertEntity = fillDummyDataEntity(salt, new AccountEntity());

    // insert
    var res1 = repository.insert(insertEntity, createdBy);
    assertNotNull(res1);

    // findById
    var insertedEntity = repository.findById(res1).get();
    assertEntity(salt, insertedEntity);
    Assertions.assertEquals(createdBy, insertedEntity.getCreatedBy());
    assertNotNull(insertedEntity.getCreatedAt());
    Assertions.assertEquals(createdBy, insertedEntity.getUpdatedBy());
    assertNotNull(insertedEntity.getUpdatedAt());

    // update
    var updateEntity = fillDummyDataEntity(++salt, new AccountEntity());
    // PK のセッタに修正する
    updateEntity.setAccountId(res1);
    var count = repository.update(updateEntity, updatedBy);
    assertEquals(1, count);

    // findById update後
    var updatedEntity = repository.findById(res1).get();
    assertEntity(salt, updatedEntity);
    Assertions.assertEquals(createdBy, updatedEntity.getCreatedBy());
    Assertions.assertEquals(insertedEntity.getCreatedAt(), updatedEntity.getCreatedAt());
    assertNotNull(updatedEntity.getCreatedAt());
    Assertions.assertEquals(updatedBy, updatedEntity.getUpdatedBy());
    Assertions.assertNotEquals(insertedEntity.getUpdatedAt(), updatedEntity.getUpdatedAt());

    // delete
    count = repository.delete(res1);
    assertEquals(1, count);
    var deletedEntity = repository.findById(res1);
    assertFalse(deletedEntity.isPresent());
  }

  AccountEntity fillDummyDataEntity(long salt, AccountEntity entity) {
    // DDLtoEntity のfill test data 列をコピー　ここから
    // （PK、created_x updated_x は不要）
    // 必要に応じて型変換（long なら「+ ""」を削除、LocalDateTime はplusHours(++salt)など
    entity.setAccountStatus(++salt +"");
    entity.setName(++salt +"");
    // DDLtoEntity のfill test data 列をコピー　ここまで
    return entity;
  }

  void assertEntity(long salt, AccountEntity entity) {
    // DDLtoEntity のtest assertion 列をコピー　ここから
    // （PK、created_x updated_x は不要）
    // 必要に応じて型変換（long なら「+ ""」を削除、OffsetDateTime はplusHours(++salt)など
    assertEquals(++salt + "", entity.getAccountStatus());
    assertEquals(++salt + "", entity.getName());
    // DDLtoEntity のtest assertion 列をコピー　ここまで
  }
}
