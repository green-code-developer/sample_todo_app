package com.example.todo.enums;

public enum TodoSearchSortEnum {
    UPDATE_DESC("updated_at desc"),
    UPDATE_ASC("updated_at asc"),
    STATUS("todo_status desc"),
    DEADLINE_DESC("deadline desc"),
    DEADLINE_ASC("deadline asc"),
    ;

    private String query;

    private TodoSearchSortEnum(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }
}
