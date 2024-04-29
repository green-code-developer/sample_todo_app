package com.example.todo.repository;

import static com.example.todo.TestSupportService.TEST_ACCOUNT_ID_1;
import static com.example.todo.TestSupportService.TEST_ACCOUNT_ID_2;
import static com.example.todo.util.DateUtil.BLANK_LOCAL_TIME;
import static org.junit.jupiter.api.Assertions.*;

import com.example.todo.TestSupportService;
import com.example.todo.entity.TodoEntity;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestTodoCrudRepository {

  private final TestSupportService testSupportService;
  private final TodoCrudRepository repository;

  @Autowired
  public TestTodoCrudRepository(
      TestSupportService testSupportService, TodoCrudRepository repository) {
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
    TodoEntity insertEntity = fillDummyDataEntity(salt, new TodoEntity());

    // insert
    Long res1 = repository.insert(insertEntity, createdBy);
    assertNotNull(res1);

    // findById
    TodoEntity insertedEntity = repository.findById(res1).get();
    assertEntity(salt, insertedEntity);
    assertEquals(createdBy, insertedEntity.getCreatedBy());
    assertNotNull(insertedEntity.getCreatedAt());
    assertEquals(createdBy, insertedEntity.getUpdatedBy());
    assertNotNull(insertedEntity.getUpdatedAt());

    // update
    TodoEntity updateEntity = fillDummyDataEntity(++salt, new TodoEntity());
    // PK のセッタに修正する
    updateEntity.setTodoId(res1);
    int count = repository.update(updateEntity, updatedBy);
    assertEquals(1, count);

    // findById update後
    TodoEntity updatedEntity = repository.findById(res1).get();
    assertEntity(salt, updatedEntity);
    assertEquals(createdBy, updatedEntity.getCreatedBy());
    assertEquals(insertedEntity.getCreatedAt(), updatedEntity.getCreatedAt());
    assertNotNull(updatedEntity.getCreatedAt());
    assertEquals(updatedBy, updatedEntity.getUpdatedBy());
    assertNotEquals(insertedEntity.getUpdatedAt(), updatedEntity.getUpdatedAt());

    // delete
    count = repository.delete(res1);
    assertEquals(1, count);
    Optional<TodoEntity> entity3 = repository.findById(res1);
    assertFalse(entity3.isPresent());
  }

  TodoEntity fillDummyDataEntity(long salt, TodoEntity entity) {
    // DDLtoEntity のfill test data 列をコピー　ここから
    // （PK、created_x updated_x は不要）
    // 必要に応じて型変換（long なら「+ ""」を削除、LocalDateTime はplusHours(++salt)など
    entity.setTodoStatus(++salt + "");
    entity.setDetail(++salt + "");
    entity.setDeadline(BLANK_LOCAL_TIME.plusHours(++salt));
    // DDLtoEntity のfill test data 列をコピー　ここまで
    return entity;
  }

  void assertEntity(long salt, TodoEntity entity) {
    // DDLtoEntity のtest assertion 列をコピー　ここから
    // （PK、created_x updated_x は不要）
    // 必要に応じて型変換（long なら「+ ""」を削除、LocalDateTime はplusHours(++salt)など
    assertEquals(++salt + "", entity.getTodoStatus());
    assertEquals(++salt + "", entity.getDetail());
    assertEquals(BLANK_LOCAL_TIME.plusHours(++salt), entity.getDeadline());
    // DDLtoEntity のtest assertion 列をコピー　ここまで
  }
}
