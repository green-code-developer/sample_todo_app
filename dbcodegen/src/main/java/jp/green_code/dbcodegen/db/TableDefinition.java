package jp.green_code.dbcodegen.db;

import jp.green_code.dbcodegen.DbCodeGenParameter;
import org.apache.commons.lang3.Strings;

import java.util.ArrayList;
import java.util.List;
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
        return columns.stream().filter(c -> !c.shouldSkipInInsert()).toList();
    }

    // UPDATE 対象となるカラム一覧
    //   PK と更新対象外(excludeUpdateColumnsByTable) を除いたカラム
    public List<ColumnDefinition> updateTargetColumns() {
        return columns.stream().filter(c -> !c.isPrimaryKey() && !c.shouldSkipInUpdate()).toList();
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
