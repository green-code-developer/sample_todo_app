package jp.green_code.dbcodegen.generator;

import jp.green_code.dbcodegen.DbCodeGenParameter;
import jp.green_code.dbcodegen.db.TableDefinition;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class TestBaseRepositoryGenerator {
    final DbCodeGenParameter param;
    final TableDefinition table;

    public TestBaseRepositoryGenerator(DbCodeGenParameter param, TableDefinition table) {
        this.param = param;
        this.table = table;
    }

    public String generateBaseTestCode() {
        var sb = new ArrayList<String>();

        // package
        sb.add("package %s;".formatted(param.baseRepositoryPackage()));
        sb.add("");

        // import
        sb.addAll(imports());
        sb.add("");

        // class
        sb.add("public abstract class %s {".formatted(table.toTestBaseRepositoryClassName()));
        sb.add("");
        sb.addAll(test());
        sb.add("");
        sb.addAll(generateTestData());
        sb.add("");
        sb.addAll(assertEntity());
        sb.add("}");
        return String.join("\n", sb);
    }

    List<String> imports() {
        var sb = new ArrayList<String>();

        var packages = new ArrayList<String>();
        packages.add(param.entityPackage + "." + table.toEntityClassName());
        // pk が1つだけの場合は、その型もimport
        table.findOnlyOnePk().map(c -> c.toJavaType().fqcn()).ifPresent(packages::add);
        table.columns.stream().filter(c -> !isBlank(c.importName())).forEach(c -> packages.add(c.importName()));
        packages.stream().distinct().sorted().map("import %s;"::formatted).forEach(sb::add);

        var statics = new ArrayList<String>();
        statics.add("org.junit.jupiter.api.Assertions.assertTrue");
        statics.add("org.junit.jupiter.api.Assertions.assertEquals");
        if (table.hasPickBySeed()) {
            statics.add("%s.%s.pickBySeed".formatted(param.baseRepositoryPackage(), param.repositoryHelperClassName));
        }
        statics.stream().distinct().sorted().map("import static %s;"::formatted).forEach(sb::add);

        return sb;
    }

    List<String> test() {
        var sb = new ArrayList<String>();
        sb.add("protected void test(%s repository) {".formatted(table.toBaseRepositoryClassName()));
        sb.add("    var seed = getInitSeed();");
        sb.add("    var data = generateTestData(seed);");
        sb.add("");
        sb.add("    // insert(upsert)");
        table.findOnlyOnePk().ifPresentOrElse(
                c -> {
                    sb.add("    data.%s(null);".formatted(c.toSetter()));
                    sb.add("    var id = repository.upsert(data);");
                    sb.add("");
                    sb.add("    // select 1回目");
                    sb.add("    var res = repository.findByPk(id);");
                    sb.add("    data.%s(id);".formatted(c.toSetter()));
                },
                () -> {
                    sb.add("    repository.upsert(data);");
                    sb.add("");
                    sb.add("    // select 1回目");
                    var pks = table.pkColumns().stream().map(c -> "data.%s()".formatted(c.toGetter())).collect(Collectors.joining(", "));
                    sb.add("    var res = repository.findByPk(%s);".formatted(pks));
                }
        );
        sb.add("    assertTrue(res.isPresent());");
        sb.add("");
        sb.add("    // insert 後の確認");
        sb.add("    var stored = res.orElseThrow();");
        for (var col : table.columns) {
            if (col.shouldSkipInInsert()) {
                sb.add("    // %s はinsert 対象外のためassertしない".formatted(col.columnName));
            } else if (col.isSetNowColumn()) {
                sb.add("    // %s はnow() を設定するカラムのためassertしない".formatted(col.columnName));
            } else {
                sb.add("    assert4%s(data.%s(), stored.%s());".formatted(col.toJavaFieldName(), col.toGetter(), col.toGetter()));
            }
        }
        sb.add("");
        sb.add("    // update(upsert)");
        sb.add("    seed++;");
        sb.add("    var data2 = generateTestData(seed);");
        table.findOnlyOnePk().ifPresentOrElse(
                c -> {
                    sb.add("    data2.%s(id);".formatted(c.toSetter()));
                    sb.add("    repository.upsert(data2);");
                    sb.add("");
                    sb.add("    // select 2回目");
                    sb.add("    var res2 = repository.findByPk(id);");
                },
                () -> {
                    for (var c : table.pkColumns()) {
                        sb.add("    data2.%s(data.%s());".formatted(c.toSetter(), c.toGetter()));
                    }
                    sb.add("    repository.upsert(data2);");
                    sb.add("");
                    sb.add("    // select 2回目");
                    var pks = table.pkColumns().stream().map(c -> "data2.%s()".formatted(c.toGetter())).collect(Collectors.joining(", "));
                    sb.add("    var res2 = repository.findByPk(%s);".formatted(pks));
                }
        );
        sb.add("    assertTrue(res2.isPresent());");
        sb.add("");
        sb.add("    // update 後の確認");
        sb.add("    var stored2 = res2.orElseThrow();");
        for (var col : table.columns) {
            if (col.shouldSkipInUpdate()) {
                sb.add("    // %s はupdate 対象外のため変更前と変わらないことを確認".formatted(col.columnName));
                sb.add("    assert4%s(stored.%s(), stored2.%s());".formatted(col.toJavaFieldName(), col.toGetter(), col.toGetter()));
            } else if (col.isSetNowColumn()) {
                sb.add("    // %s はnow() を設定するカラムのためassertしない".formatted(col.columnName));
            } else {
                sb.add("    assert4%s(data2.%s(), stored2.%s());".formatted(col.toJavaFieldName(), col.toGetter(), col.toGetter()));
            }
        }
        sb.add("");
        sb.add("    // delete");
        table.findOnlyOnePk().ifPresentOrElse(
                c -> sb.add("    var deleteCount = repository.deleteByPk(id);"),
                () -> {
                    var pks = table.pkColumns().stream().map(c -> "data2.%s()".formatted(c.toGetter())).collect(Collectors.joining(", "));
                    sb.add("    var deleteCount = repository.deleteByPk(%s);".formatted(pks));
                }
        );
        sb.add("    assertEquals(1, deleteCount);");
        sb.add("    // select 3回目");
        table.findOnlyOnePk().ifPresentOrElse(
                c -> sb.add("    var stored3 = repository.findByPk(id);"),
                () -> {
                    var pks = table.pkColumns().stream().map(c -> "data2.%s()".formatted(c.toGetter())).collect(Collectors.joining(", "));
                    sb.add("    var stored3 = repository.findByPk(%s);".formatted(pks));
                }
        );
        sb.add("    assertTrue(stored3.isEmpty());");

        sb.add("}");
        sb.add("");
        sb.add("protected int getInitSeed() {");
        sb.add("    return 1;");
        sb.add("}");
        return sb.stream().map(s -> isBlank(s) ? s : "    " + s).toList();
    }

    List<String> generateTestData() {
        var sb = new ArrayList<String>();
        sb.add("public %s generateTestData(int seed) {".formatted(table.toEntityClassName()));
        sb.add("    var entity = new %s();".formatted(table.toEntityClassName()));
        for (var col : table.columns) {
            var plusplus = table.columns.getLast() == col ? "" : "++";
            sb.add("    entity.%s(generateTestData4%s(seed%s));".formatted(col.toSetter(), col.toJavaFieldName(), plusplus));
        }
        sb.add("    return entity;");
        sb.add("}");
        for (var col : table.columns) {
            sb.add("");
            if (isBlank(col.toJavaType().generateDateSnippet())) {
                throw new RuntimeException("no javaTestSnippet %s.%s".formatted(table.tableName, col.columnName));
            } else {
                sb.add("protected %s generateTestData4%s(int seed) {".formatted(col.javaSimpleName(), col.toJavaFieldName()));
                sb.add("    %s".formatted(col.toJavaType().generateDateSnippet()));
                sb.add("}");
            }
        }
        return sb.stream().map(s -> isBlank(s) ? s : "    " + s).toList();
    }

    List<String> assertEntity() {
        var sb = new ArrayList<String>();
        for (var col : table.columns) {
            sb.add("");
            sb.add("protected void assert4%s(%s expected, %s value) {".formatted(col.toJavaFieldName(), col.javaSimpleName(), col.javaSimpleName()));
            if (isBlank(col.toJavaType().assertSnippet())) {
                sb.add("    assertEquals(expected, value);");
            } else {
                sb.add("    %s".formatted(col.toJavaType().assertSnippet()));
            }
            sb.add("}");
        }
        return sb.stream().map(s -> isBlank(s) ? s : "    " + s).toList();
    }
}
