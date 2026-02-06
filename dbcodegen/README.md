# DbCodeGen

## 機能概要

DbCodeGen は、PostgreSQL + Spring JDBC 環境において
「SQL は手で書きたいが、定型的な Entity / Repository / Test を自動生成したい」
という前提で作られたコード生成用 CLI ツールです。

- コマンドライン実行型
- PostgreSQL のみ対応
- Spring JDBC 前提
- Entity, Repository, TestRepository 自動生成
- Enum 対応可能
- Update Insert 除外カラム指定可能

## 導入と動かし方

1. Spring JDBC を持つプロジェクトにこの dbcodegen フォルダをコピー
2. param.yml を記載

   データベース接続情報、パッケージ名、最上位フォルダ、を指定

3. ツール実行

   ```bash
   gradle clean run
   ```

   Linux, MacOS 環境であれば```make``` も可

4. 指定したパッケージにJava コードが作成される

## Repository の使い方

### upsert(Entity entity)

データがなければinsert、あればupdate を行います。
プライマリーキーがないテーブルに対しては、insert のみ行います。

プライマリーキーが1つだけのテーブルは、戻り値が更新後のプライマリーキーとなります。
これにより、自動採番されたid を戻り値として取得できます。

not null 制約ありかつ初期値を持つカラムに対して、entity 中のフィールドの値がnull であった場合は、そのカラムがinsert やupdate の対象から外されます。
対象から外されることでDB カラムに定義された初期値を有効に使えるようになります。

```java
var account = new AccountEntity();
account.setAccountId(null); // PK フィールドの値がnull
account.setName("green-code-user");
var id = accountRepository.upsert(account); // insert
account.setAccountId(id); // 自動採番されたid をセット
account.setName("green-code-userB");
accountRepository.upsert(account); // update
```

### Optional&lt;Entity&gt; findByPk(pk)

指定されたプライマリーキーに対応するレコードを取得します。

### int deleteByPk(pk)

指定されたプライマリーキーに対応するレコードを削除します。
戻り値は削除された件数です。

### Columns

テーブルが持つカラム名の定数群です。
使用が必須ではありませんが、SQL 作成時にカラム名の打ち間違いを防げます。

### ALL_COLUMNS

全てのカラム名をカンマで区切ったものです。
select * from table と書きたい時に、* の代わりにこの定数を使います。
カラム名に加えて型変換が付与されています。
例）col_xml::text

### RepositoryHelper helper

NamedParameterJdbcTemplate をラップして短く記載できるようにしたものです。

helper.list()
--> 複数件取得（List&lt;Entity&gt;型）

helper.one()
--> 先頭1件のみ取得（Optional&lt;Entity&gt;型）

helper.exec()
--> 実行のみで戻り値なし。update やDDL 文が対象

helper.count()
--> Long 型の1カラムを取得するselect 文が対象。select count(*) ... を想定

## TestRepository の使い方

param.yml testTargetTable にテスト対象のテーブル名を記載するとテストコードが生成されます。
insert, select, update, select, delete, select を順番に行います。

テストデータは generateTestData4{フィールド名}() にて作成されます。
必要に応じてoverride してください。

外部キー制約があるとテストが難しくなります。依存するレコードが必要な場合は@BeforeEach などを使って作成する必要があります。

データの確認は assert4{フィールド名}() にて行います。こちらも必要に応じてoverride してください。

## Base クラス

Entity, Repository, TestRepository いずれも Base クラスとその実体クラスという構成となっています。

ツールを実行すると、Base クラスは毎回再作成されますが、実体クラスは初回以外変更しません。

param.yml のforceOverwriteImplementation をtrue にすると実体クラスも再作成されます。（デフォルトfalse）

## 確認済みの構成

- Spring Boot 3.5.10
- Spring JDBC
- PostgreSQL 17

## できないこと

- Postgres 以外のデータベース
- Spring とSpring JDBC がない環境での動作
- ORM (Object Relation Mapping) 
SQL を直接書かずJava でクエリーを構築すること

## 便利な使い方

### Enum 型を追加する

param.yml のenumJavaTypeMappings に設定を入れてCI ツールを実行します。

設定例
- DB enum 名 : todo_status
- Java Enum クラス : jp.green_code.todo.enums.TodoStatusEnum

このように記載します。
```yml
# param.yml
enumJavaTypeMappings:
  todo_status: jp.green_code.todo.enums.TodoStatusEnum
```
テーブルを問わず、todo_status 型カラムは全てこのEnum クラスにマッピングされます。

参考までに、todo_status のDDL 文とEnum クラスを残しておきます。
```sql
-- todo_status DDL 文
CREATE TYPE todo_status AS ENUM ('NEW', 'DOING', 'DONE', 'DELETED');
```
```java
// TodoStatusEnum.java
public enum TodoStatusEnum { NEW, DOING, DONE, DELETED; }
```

### 作成者カラム、作成日時カラムの管理

作成者カラム、作成日時カラムのように、初回Insert 時以外は更新を行わないカラムについては、param.yml excludeUpdateColumnsByTable に登録すると良いです。
こちらに登録されたカラムはUpdate 時に更新されなくなります。

設定例
- 作成者カラム : created_by
- 作成日時カラム : created_at
```yml
# param.yml
excludeUpdateColumnsByTable:
   "*":
      - created_at
      - created_by
```
"*" は全てのテーブルを意味します。個別のテーブルを指定する場合は、テーブル名を記載します。

また、作成日時カラムにデフォルト値を現在時刻として設定した場合、Insert でこのカラムを指定する必要がありません。
param.yml excludeInsertColumnsByTable に登録されたカラムは、Insert 時に指定されません。

設定例
- 作成日時カラム : created_at
```yml
# param.yml
excludeInsertColumnsByTable:
   "*":
      - created_at
```
```sql
-- todo DDL 文
create table TODO (
  -- 中略
  CREATED_AT timestamp with time zone not null default now(),
```

### Insert, Update 時にデータベースの時刻 now() を指定したい

param.yml のsetNowColumnsByTable に設定すると、そのカラムの値はSQL の now() に置き換わります。
指定されたカラムはjava で値を更新することができなくなります。

設定例
```yml
# param.yml
setNowColumnsByTable:
   "*":
      - updated_at
      - created_at
```

### テストデータ作成で固定値を指定したい

generateTestData4{フィールド名}() をoverride することで実現できます。

例）Base クラス
```java
// TestBaseTodoRepository.java
public AccountEntity generateTestData(int seed) {
    var entity = new AccountEntity();
    entity.setAccountId(generateTestData4accountId(seed++));
    // 中略
    return entity;
}
protected Long generateTestData4accountId(int seed) {
   return (long) seed;
}
```

Override した実体クラス
```java
// TestTodoRepository.java
@Override
protected Long generateTestData4updatedBy(int seed) {
    return -1L; // 固定値
}
```

## DB 型とJava 型の変換表

| 区分     | PostgreSQL 型                | Java 型                   | 備考              |
|--------|-----------------------------|--------------------------|-----------------|
| 数値     | smallint                    | java.lang.Short          |                 |
| 数値     | int2                        | java.lang.Short          |                 |
| 数値     | smallserial                 | java.lang.Short          |                 |
| 数値     | integer                     | java.lang.Integer        |                 |
| 数値     | int4                        | java.lang.Integer        |                 |
| 数値     | serial                      | java.lang.Integer        |                 |
| 数値     | bigint                      | java.lang.Long           |                 |
| 数値     | int8                        | java.lang.Long           |                 |
| 数値     | bigserial                   | java.lang.Long           |                 |
| 数値     | real                        | java.lang.Float          |                 |
| 数値     | float4                      | java.lang.Float          |                 |
| 数値     | double precision            | java.lang.Double         |                 |
| 数値     | float8                      | java.lang.Double         |                 |
| 数値     | numeric                     | java.math.BigDecimal     |                 |
| 論理     | boolean                     | java.lang.Boolean        |                 |
| 論理     | bool                        | java.lang.Boolean        |                 |
| 文字列    | character                   | java.lang.String         |                 |
| 文字列    | bpchar                      | java.lang.String         |                 |
| 文字列    | character varying           | java.lang.String         |                 |
| 文字列    | varchar                     | java.lang.String         |                 |
| 文字列    | text                        | java.lang.String         |                 |
| 日付     | date                        | java.time.LocalDate      |                 |
| 時刻     | time                        | java.time.LocalTime      |                 |
| 時刻     | time without time zone      | java.time.LocalTime      |                 |
| 時刻     | time with time zone         | java.time.OffsetTime     |                 |
| 時刻     | timetz                      | java.time.OffsetTime     |                 |
| 時刻     | timestamp                   | java.time.LocalDateTime  |                 |
| 時刻     | timestamp without time zone | java.time.LocalDateTime  |                 |
| 時刻     | timestamp with time zone    | java.time.OffsetDateTime |                 |
| 時刻     | timestamptz                 | java.time.OffsetDateTime |                 |
| 時刻     | interval                    | java.lang.Long           | 秒（epoch）扱いが難しい  |
| バイナリ   | bytea                       | byte[]                   |                 |
| 文字列    | uuid                        | java.util.UUID           |                 |
| 文字列    | json                        | java.lang.String         | INSERT 時 ::jsonb |
| 文字列    | jsonb                       | java.lang.String         | INSERT 時 ::jsonb |
| 文字列    | xml                         | java.lang.String         | INSERT 時 ::xml  |
| ネットワーク | inet                        | java.lang.String         | INSERT 時 ::inet |
| ネットワーク | cidr                        | java.lang.String         | INSERT 時 ::cidr |
| ネットワーク | macaddr                     | java.lang.String         | INSERT 時 ::macaddr |
| 幾何     | point                       | java.lang.String         | SELECT 時 ::text |
| 幾何     | line                        | java.lang.String         | SELECT 時 ::text |
| 幾何     | lseg                        | java.lang.String         | SELECT 時 ::text |
| 幾何     | box                         | java.lang.String         | SELECT 時 ::text |
| 幾何     | path                        | java.lang.String         | SELECT 時 ::text |
| 幾何     | polygon                     | java.lang.String         | SELECT 時 ::text |
| 幾何     | circle                      | java.lang.String         | SELECT 時 ::text |

### 対応外の型

| 区分   | DB 型          | Java 型 |
|------|---------------|--------|
| 金額   | money         | 対応外    |
| 全文検索 | tsvector      | 対応外    |
| 全文検索 | tsquery       | 対応外    |
| ビット  | bit           | 対応外    |
| 内部   | pg_lsn        | 対応外    |
| 内部   | txid_snapshot | 対応外    |
| その他  | 記載のないもの       | 対応外    |
