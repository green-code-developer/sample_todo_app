package jp.green_code.todo.repository;

import jp.green_code.todo.repository.base.BaseColumnDefinition;

public class ColumnDefinition extends BaseColumnDefinition {
    public ColumnDefinition(String columnName, String javaFieldName, String javaFqcn, String dbTypeName, Integer jdbcType, Integer columnSize, Integer primaryKeySeq, boolean nullable, boolean hasDefault, String dbParamTemplate, String dbSelectTemplate, boolean isSetNow, boolean shouldSkipInUpdate) {
        super(columnName, javaFieldName, javaFqcn, dbTypeName, jdbcType, columnSize, primaryKeySeq, nullable, hasDefault, dbParamTemplate, dbSelectTemplate, isSetNow, shouldSkipInUpdate);
    }
}