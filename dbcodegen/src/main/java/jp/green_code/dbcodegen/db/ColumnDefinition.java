package jp.green_code.dbcodegen.db;

import static jp.green_code.dbcodegen.DbCodeGenUtil.toCamelCase;
import static org.apache.commons.lang3.StringUtils.isBlank;

public class ColumnDefinition {
    public String columnName;
    public String dbTypeName;
    public Integer jdbcType;
    public Integer columnSize;
    public boolean nullable;
    public String primaryKeyName;
    public Short primaryKeySeq;
    public String defaultExpression;

    public String toLogString() {
        return columnName + " [" + dbTypeName + "] " + (nullable ? "nullable" : "nonnull") + " " + (isBlank(primaryKeyName) ? "" : "pk(" + primaryKeySeq + ") default[" + defaultExpression + "]");
    }

    public String toJavaFieldName() {
        return toCamelCase(columnName, false);
    }

    public boolean hasDefault() {
        return !isBlank(defaultExpression);
    }

    public boolean isPrimaryKey() {
        return !isBlank(primaryKeyName);
    }

    public boolean isInsertOmittable() {
        return !nullable && hasDefault();
    }

    public String toGetter() {
        String methodName = toCamelCase(columnName, true);
        return "get%s".formatted(methodName);
    }

    public String toSetter() {
        String methodName = toCamelCase(columnName, true);
        return "set%s".formatted(methodName);
    }

    public JavaType toJavaType() {
        return DbTypeMapper.map(dbTypeName);
    }

    public String javaSimpleName() {
        var javaFqcn = toJavaType().fqcn();
        int idx = javaFqcn.lastIndexOf('.');
        return (idx >= 0) ? javaFqcn.substring(idx + 1) : javaFqcn;
    }

    public String importName() {
        // fqcn と同じだがプリミティブ型の場合はnull を返す
        var javaFqcn = toJavaType().fqcn();
        return javaFqcn.contains(".") ? javaFqcn : null;
    }

    public String toSelectColumn() {
        if (isBlank(toJavaType().dbSelectTemplate())) {
            return columnName;
        } else {
            return toJavaType().dbSelectTemplate().replace("{colName}", columnName);
        }
    }
}
