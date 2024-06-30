package jp.green_code.todo.repository;

import java.util.Optional;
import jp.green_code.todo.entity.TodoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

public interface TodoJpaRepository extends Repository<TodoEntity, Long> {

  TodoEntity save(TodoEntity todo);

  Optional<TodoEntity> findById(long id);

  String FORM_SEARCH = """
      from todo t
      where
        (:word is null or :word = '' or t.detail like concat('%',:word,'%'))
        and (:status is null or :status = '' or t.todo_status = :status)
        and (:deadlineFrom is null or :deadlineFrom = '' or cast(:deadlineFrom as timestamp) <= t.deadline)
        and (:deadlineTo is null or :deadlineTo = '' or t.deadline <= cast(:deadlineTo as timestamp))
      """;

  @Query(value = "select * " + FORM_SEARCH, countQuery = "select count(*) " + FORM_SEARCH, nativeQuery = true)
  Page<TodoEntity> findByForm(PageRequest pageRequest, String word, String status, String deadlineFrom,
      String deadlineTo);
}
