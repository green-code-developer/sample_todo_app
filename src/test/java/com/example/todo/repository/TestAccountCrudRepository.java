package com.example.todo.repository;

import static com.example.todo.TestSupportService.TEST_ACCOUNT_ID_1;
import static com.example.todo.TestSupportService.TEST_ACCOUNT_ID_2;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.example.todo.TestSupportService;
import com.example.todo.entity.AccountEntity;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
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
    long createdBy = TEST_ACCOUNT_ID_1;
    long updatedBy = TEST_ACCOUNT_ID_2;
    long salt = 0;

    // 初期データ
    AccountEntity insertEntity = fillDummyDataEntity(salt, new AccountEntity());

    // insert
    Long res1 = repository.insert(insertEntity, createdBy);
    assertNotNull(res1);

    // findById
    AccountEntity insertedEntity = repository.findById(res1).get();
    assertEntity(salt, insertedEntity);
    assertEquals(createdBy, insertedEntity.getCreatedBy());
    assertNotNull(insertedEntity.getCreatedAt());
    assertEquals(createdBy, insertedEntity.getUpdatedBy());
    assertNotNull(insertedEntity.getUpdatedAt());

    // update
    AccountEntity updateEntity = fillDummyDataEntity(++salt, new AccountEntity());
    // PK のセッタに修正する
    updateEntity.setAccountId(res1);
    int count = repository.update(updateEntity, updatedBy);
    assertEquals(1, count);

    // findById update後
    AccountEntity updatedEntity = repository.findById(res1).get();
    assertEntity(salt, updatedEntity);
    assertEquals(createdBy, updatedEntity.getCreatedBy());
    assertEquals(insertedEntity.getCreatedAt(), updatedEntity.getCreatedAt());
    assertNotNull(updatedEntity.getCreatedAt());
    assertEquals(updatedBy, updatedEntity.getUpdatedBy());
    assertNotEquals(insertedEntity.getUpdatedAt(), updatedEntity.getUpdatedAt());

    // delete
    count = repository.delete(res1);
    assertEquals(1, count);
    Optional<AccountEntity> entity3 = repository.findById(res1);
    assertFalse(entity3.isPresent());
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
    // 必要に応じて型変換（long なら「+ ""」を削除、LocalDateTime はplusHours(++salt)など
    assertEquals(++salt + "", entity.getAccountStatus());
    assertEquals(++salt + "", entity.getName());
    // DDLtoEntity のtest assertion 列をコピー　ここまで
  }
}
