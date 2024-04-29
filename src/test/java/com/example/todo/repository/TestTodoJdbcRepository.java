package com.example.todo.repository;

import static java.lang.String.join;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.todo.web.form.TodoSearchForm;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestTodoJdbcRepository {

    private final TodoJdbcRepository repository;

    @Autowired
    public TestTodoJdbcRepository(TodoJdbcRepository repository) {
        this.repository = repository;
    }

    @Test
    public void toFindSql() {

        // 何も条件なし
        TodoSearchForm form = new TodoSearchForm();
        List<String> sql = repository.toFindSql(form);
        assertEquals("where 1=1", join(" ", sql));

        // word
        form = new TodoSearchForm();
        form.setWord("hoge");
        sql = repository.toFindSql(form);
        assertEquals("where 1=1 and detail like concat('%',:word,'%')", join(" ", sql));

        // status
        form = new TodoSearchForm();
        form.setStatus("hoge");
        sql = repository.toFindSql(form);
        assertEquals("where 1=1 and todo_status = :status", join(" ", sql));

        // 締切下限
        form = new TodoSearchForm();
        form.setDeadlineFrom("hoge");
        sql = repository.toFindSql(form);
        assertEquals("where 1=1 and :deadlineFrom::timestamp <= deadline", join(" ", sql));

        // 締切上限
        form = new TodoSearchForm();
        form.setDeadlineTo("hoge");
        sql = repository.toFindSql(form);
        assertEquals("where 1=1 and deadline <= :deadlineTo::timestamp", join(" ", sql));
    }
}