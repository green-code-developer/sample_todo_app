package jp.green_code.dbcodegen.generator;

import jp.green_code.dbcodegen.DbCodeGenParameter;
import jp.green_code.dbcodegen.db.ColumnDefinition;
import jp.green_code.dbcodegen.db.TableDefinition;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.joining;
import static org.apache.commons.lang3.StringUtils.isBlank;

public class BaseRepositoryGenerator {
    final DbCodeGenParameter param;
    final TableDefinition table;

    public BaseRepositoryGenerator(DbCodeGenParameter param, TableDefinition table) {
        this.param = param;
        this.table = table;
    }

    public String generateBaseRepositoryCode() {
        var sb = new ArrayList<String>();

        // package
        sb.add("package %s;".formatted(param.baseRepositoryPackage()));
        sb.add("");

        // import
        sb.addAll(imports());
        sb.add("");

        // class
        sb.add("/**");
        sb.add(" * Table: %s".formatted(table.tableName));
        sb.add(" */");
        sb.add("public abstract class %s {".formatted(table.toBaseRepositoryClassName()));
        sb.add("");
        sb.add("    protected final %s helper;".formatted(param.repositoryHelperClassName));
        // 全カラム名をカンマでつなげた定数。select * の代わりに使う
        sb.add("    public static final String ALL_COLUMNS = \"%s\";".formatted(table.columns.stream().map(ColumnDefinition::toSelectColumn).collect(joining(", "))));
        sb.add("");
        sb.addAll(columns());
        sb.add("");
        sb.add("    public %s(%s helper) {".formatted(table.toBaseRepositoryClassName(), param.repositoryHelperClassName));
        sb.add("        this.helper = helper;");
        sb.add("    }");
        sb.add("");
        sb.addAll(upsert());
        sb.add("");
        sb.addAll(entityToParam());
        if (!table.pkColumns().isEmpty()) {
            // pk がない場合は、findByPk とdeleteByPk は作れない
            sb.add("");
            sb.addAll(findByPk());
            sb.add("");
            sb.addAll(deleteByPk());
        }
        sb.add("}");
        return String.join("\n", sb);
    }

    List<String> imports() {
        var packages = new ArrayList<String>();
        packages.add(param.entityPackage + "." + table.toEntityClassName());
        packages.add("java.util.Map");
        packages.add("java.util.ArrayList");
        packages.add("java.util.HashMap");
        packages.add("java.util.Optional");
        // pk が1つだけの場合は、その型もimport
        table.findOnlyOnePk().map(c -> c.toJavaType().fqcn()).ifPresent(packages::add);
        return packages.stream().distinct().sorted().map("import %s;"::formatted).toList();
    }

    List<String> columns() {
        // カラム名の定数を提供
        var sb = new ArrayList<String>();
        sb.add("    public static final class Columns {");
        for (var col : table.columns) {
            sb.add("        public static final String %s = \"%s\";".formatted(col.columnName.toUpperCase(), col.columnName));
        }
        sb.add("        private Columns() {}");
        sb.add("    }");
        return sb;
    }

    List<String> upsert() {
        var sb = new ArrayList<String>();
        var pkType = table.findOnlyOnePk().map(c -> "%s".formatted(c.javaSimpleName())).orElse("int");
        sb.add("public %s upsert(%s entity) {".formatted(pkType, table.toEntityClassName()));
        sb.add("    var sql = new ArrayList<String>();");
        sb.add("    sql.add(\"insert into %s\");".formatted(table.tableName));
        sb.add("    var insertColumns = new ArrayList<String>();");
        for (var col : table.insertTargetColumns()) {
            if (col.isInsertOmittable()) {
                sb.add("    if (entity.%s() != null) {".formatted(col.toGetter()));
                sb.add("        insertColumns.add(\"%s\");".formatted(col.columnName));
                sb.add("    }");
            } else {
                sb.add("    insertColumns.add(\"%s\");".formatted(col.columnName));
            }
        }
        var indent = "";
        if (table.hasAllDefaultValue()) {
            indent = "    ";
            sb.add("    if (insertColumns.isEmpty()) {");
            sb.add("        // 全てのカラムがデフォルト値の場合");
            sb.add("        sql.add(\"DEFAULT VALUES\");");
            sb.add("    } else {");
        }
        sb.add(indent + "    sql.add(\"(%s)\".formatted(String.join(\", \", insertColumns)));");
        sb.add(indent + "    sql.add(\"values\");");
        sb.add(indent + "    var insertValues = new ArrayList<String>();");
        for (var col : table.insertTargetColumns()) {
            var dbTypeTemplate = (isBlank(col.toJavaType().dbParamTemplate()) ? ":%s" : col.toJavaType().dbParamTemplate()).formatted(col.toJavaFieldName());
            String insertValue;
            if (col.isSetNowColumn()) {
                insertValue = "insertValues.add(\"now()\");";
            } else {
                insertValue = "insertValues.add(\"%s\");".formatted(dbTypeTemplate);
            }
            if (col.isInsertOmittable()) {
                sb.add(indent + "    if (entity.%s() != null) {".formatted(col.toGetter()));
                sb.add(indent + "        " + insertValue);
                sb.add(indent + "    }");
            } else {
                sb.add(indent + "    " + insertValue);
            }
        }
        sb.add(indent + "    sql.add(\"(%s)\".formatted(String.join(\", \", insertValues)));");
        // pk なしの場合はon conflict 以下を作成しない
        if (!table.pkColumns().isEmpty()) {
            sb.add(indent + "        sql.add(\"on conflict (\");");
            for (var col : table.pkColumns()) {
                var comma = table.pkColumns().getLast() == col ? "" : ",";
                sb.add(indent + "        sql.add(\"    %s%s\");".formatted(col.columnName, comma));
            }
            sb.add(indent + "    var updateValues = new ArrayList<String>();");
            for (var col : table.updateTargetColumns()) {
                String updateValue;
                if (col.isSetNowColumn()) {
                    updateValue = "updateValues.add(\"%s = now()\");".formatted(col.columnName);
                } else {
                    updateValue = "updateValues.add(\"%s = EXCLUDED.%s\");".formatted(col.columnName, col.columnName);
                }
                if (col.isInsertOmittable()) {
                    sb.add(indent + "    if (entity.%s() != null) {".formatted(col.toGetter()));
                    sb.add(indent + "        " + updateValue);
                    sb.add(indent + "    }");
                } else {
                    sb.add(indent + "    " + updateValue);
                }
            }
            sb.add(indent + "    if (updateValues.isEmpty()) {");
            sb.add(indent + "        sql.add(\") do nothing\");");
            sb.add(indent + "    } else {");
            sb.add(indent + "        sql.add(\") do update set\");");
            sb.add(indent + "        sql.add(String.join(\", \", updateValues));");
            sb.add(indent + "    }");
        }
        if (table.hasAllDefaultValue()) {
            sb.add("    }");
        }
        table.findOnlyOnePk().ifPresent(c -> sb.add("    sql.add(\"returning %s\");".formatted(c.columnName)));
        sb.add("");
        sb.add("    var param = entityToParam(entity);");
        table.findOnlyOnePk().ifPresentOrElse(
                c -> sb.add("    return helper.one(sql, param, %s.class).orElseThrow();".formatted(c.javaSimpleName())),
                () -> sb.add("    return helper.exec(sql, param);")
        );
        sb.add("}");
        return sb.stream().map(s -> isBlank(s) ? s : "    " + s).toList();
    }

    List<String> entityToParam() {
        var sb = new ArrayList<String>();
        sb.add("public static Map<String, Object> entityToParam(%s entity) {".formatted(table.toEntityClassName()));
        sb.add("    var param = new HashMap<String, Object>();");
        for (var col : table.columns) {
            var valueExpressionTemplate = isBlank(col.toJavaType().javaCastSnippetInEntityToParam()) ? "entity.%s()" : col.toJavaType().javaCastSnippetInEntityToParam();
            valueExpressionTemplate = valueExpressionTemplate.formatted(col.toGetter());
            sb.add("    param.put(\"%s\", %s);".formatted(col.toJavaFieldName(), valueExpressionTemplate));
        }
        sb.add("    return param;");
        sb.add("}");
        return sb.stream().map(s -> isBlank(s) ? s : "    " + s).toList();
    }

    List<String> findByPk() {
        var sb = new ArrayList<String>();
        var pkArgs = toPkArgs();
        sb.add("public Optional<%s> findByPk(%s) {".formatted(table.toEntityClassName(), pkArgs));
        sb.add("    var sql = new ArrayList<String>();");
        sb.add("    sql.add(\"select %s\".formatted(ALL_COLUMNS));");
        sb.add("    sql.add(\"from %s\");".formatted(table.tableName));
        sb.add("    sql.add(\"where\");");
        for (var col : table.pkColumns()) {
            var comma = table.pkColumns().getLast() == col ? "" : " AND";
            sb.add("    sql.add(\"    %s = :%s%s\");".formatted(col.columnName, col.toJavaFieldName(), comma));
        }
        sb.add("");
        sb.add("    var param = new HashMap<String, Object>();");
        for (var col : table.pkColumns()) {
            sb.add("    param.put(\"%s\", %s);".formatted(col.toJavaFieldName(), col.toJavaFieldName()));
        }
        sb.add("");
        sb.add("    return helper.one(sql, param, %s.class);".formatted(table.toEntityClassName()));
        sb.add("}");
        return sb.stream().map(s -> isBlank(s) ? s : "    " + s).toList();
    }

    List<String> deleteByPk() {
        var sb = new ArrayList<String>();
        var pkArgs = toPkArgs();
        sb.add("public int deleteByPk(%s) {".formatted(pkArgs));
        sb.add("    var sql = new ArrayList<String>();");
        sb.add("    sql.add(\"delete from %s\");".formatted(table.tableName));
        sb.add("    sql.add(\"where\");");
        for (var col : table.pkColumns()) {
            var comma = table.pkColumns().getLast() == col ? "" : " AND";
            sb.add("    sql.add(\"    %s = :%s%s\");".formatted(col.columnName, col.toJavaFieldName(), comma));
        }
        sb.add("");
        sb.add("    var param = new HashMap<String, Object>();");
        for (var col : table.pkColumns()) {
            sb.add("    param.put(\"%s\", %s);".formatted(col.toJavaFieldName(), col.toJavaFieldName()));
        }
        sb.add("");
        sb.add("    return helper.exec(sql, param);");
        sb.add("}");
        return sb.stream().map(s -> isBlank(s) ? s : "    " + s).toList();
    }

    String toPkArgs() {
        return table.pkColumns().stream().map(c -> c.javaSimpleName() + " " + c.toJavaFieldName()).collect(joining(", "));
    }
}
