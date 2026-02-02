package jp.green_code.dbcodegen.db;

import jp.green_code.dbcodegen.DbCodeGenParameter;
import org.apache.commons.lang3.Strings;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Comparator.comparing;
import static jp.green_code.dbcodegen.DbCodeGenUtil.toCamelCase;

public class TableDefinition {
    final DbCodeGenParameter param;

    public TableDefinition(DbCodeGenParameter param) {
        this.param = param;
    }

    public String tableName;
    public List<ColumnDefinition> columns = new ArrayList<>();

    private String toClassName() {
        return toCamelCase(tableName, true);
    }

    public String toBaseEntityClassName() {
        return toCamelCase(param.basePackageName, true) + param.entityClassNamePrefix + toClassName() + param.entityClassNameSuffix;
    }

    public String toEntityClassName() {
        return param.entityClassNamePrefix + toClassName() + param.entityClassNameSuffix;
    }

    public String toBaseRepositoryClassName() {
        return toCamelCase(param.basePackageName, true) + param.repositoryClassNamePrefix + toClassName() + param.repositoryClassNameSuffix;
    }

    public String toRepositoryClassName() {
        return param.repositoryClassNamePrefix + toClassName() + param.repositoryClassNameSuffix;
    }

    public String toTestBaseRepositoryClassName() {
        return param.testRepositoryClassNamePrefix + toBaseRepositoryClassName() + param.testRepositoryClassNameSuffix;
    }

    public String toTestRepositoryClassName() {
        return param.testRepositoryClassNamePrefix + toRepositoryClassName() + param.testRepositoryClassNameSuffix;
    }

    // PK が一つだけならそのカラムを返す。0個または2個以上の場合は返さない
    public Optional<ColumnDefinition> findOnlyOnePk() {
        var pkCols = columns.stream().filter(ColumnDefinition::isPrimaryKey).toList();
        return pkCols.size() == 1 ? Optional.of(pkCols.getFirst()) : Optional.empty();
    }

    // PK のカラム全てを返す
    public List<ColumnDefinition> pkColumns() {
        return columns.stream().filter(ColumnDefinition::isPrimaryKey).sorted(comparing(c -> c.primaryKeySeq)).toList();
    }

    // INSERT 対象となるカラム一覧
    //   PK と更新対象外(excludeInsertColumnsByTable) を除いたカラム
    public List<ColumnDefinition> insertTargetColumns() {
        return columns.stream().filter(c -> !shouldSkipInInsert(c)).toList();
    }

    // INSERT 対象外カラム一覧
    //   INSERT（excludeInsertColumnsByTable）に含まれるカラム
    public List<ColumnDefinition> insertExcludedColumns() {
        return columns.stream().filter(this::shouldSkipInInsert).toList();
    }

    // INSERT 対象外判定
    public boolean shouldSkipInInsert(ColumnDefinition col) {
        return mapContainsColumn(param.excludeInsertColumnsByTable, tableName, col.columnName);
    }

    // UPDATE 対象となるカラム一覧
    //   PK と更新対象外(excludeUpdateColumnsByTable) を除いたカラム
    public List<ColumnDefinition> updateTargetColumns() {
        return columns.stream().filter(c -> !c.isPrimaryKey() && !shouldSkipInUpdate(c)).toList();
    }

    // UPDATE 対象外カラム一覧
    //   UPDATE対象外（excludeUpdateColumnsByTable）に含まれるカラム
    public List<ColumnDefinition> updateExcludedColumns() {
        return columns.stream().filter(this::shouldSkipInUpdate).toList();
    }

    // UPDATE 対象外判定
    public boolean shouldSkipInUpdate(ColumnDefinition col) {
        return mapContainsColumn(param.excludeUpdateColumnsByTable, tableName, col.columnName);
    }

    // テスト対象となるカラム一覧
    //   テスト対象外（testSkipColumnsByTable）を除いたカラム
    public List<ColumnDefinition> testTargetColumns() {
        return columns.stream().filter(c -> !this.shouldSkipColumnInTest(c)).toList();
    }

    // テスト対象外判定
    public boolean shouldSkipColumnInTest(ColumnDefinition col) {
        return mapContainsColumn(param.testSkipColumnsByTable, tableName, col.columnName);
    }

    // map にカラムが含まれるか汎用判定
    static boolean mapContainsColumn(Map<String, List<String>> map, String tableName, String columnName) {
        // テーブル名に「*」で登録されているカラム
        if (map.containsKey("*") && map.get("*").contains(columnName)) {
            return true;
        }
        // テーブル名とカラム名で登録されている
        return map.containsKey(tableName) && map.get(tableName).contains(columnName);
    }

    // テスト対象外テーブル判定
    public boolean shouldSkipTableInTest() {
        return !param.testTargetTable.contains(tableName);
    }

    // テストデータ作成にpickBySeed を使っているカラムがあるか判定（enum がこれに該当する）
    public boolean hasPickBySeed() {
        return columns.stream().anyMatch(c ->
                Strings.CS.contains(c.toJavaType().generateDateSnippet(), "pickBySeed"));
    }

    // 全てのカラムが省略可能か判定
    public boolean hasAllDefaultValue() {
        return columns.stream().allMatch(ColumnDefinition::isInsertOmittable);
    }
}
