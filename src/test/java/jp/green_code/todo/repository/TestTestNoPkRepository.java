package jp.green_code.todo.repository;

import jp.green_code.todo.entity.TestNoPkEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class TestTestNoPkRepository {

    @Autowired
    TestNoPkRepository repository;

    @Test
    void test() {
        var entity = new TestNoPkEntity();
        entity.setColText(new Date() + "");

        var res = repository.upsert(entity);
        assertEquals(1, res);
    }
}