# やること（Todo）仕様書

## 1. スコープ

本書は Todo アプリの画面・機能・データ仕様を定義する。

**対象**

- 一覧／検索、登録、更新の各機能
- 上記に対応する Controller / Service / DTO / Repository の実装

**対象外**

- `domain/entity/base`、`domain/repository/base` の自動生成コード（spring-jdbc-codegen が生成するため仕様書では扱わない）
- 認証・認可（デモアプリのため未実装。全 URL が未認証で参照・更新可能）
- 削除機能（未実装）

## 2. 構成

### 2.1 技術構成

| 種別 | 採用技術 |
|---|---|
| 言語 | Java 21 |
| フレームワーク | Spring Boot 4 系 |
| テンプレートエンジン | Thymeleaf |
| データベース | PostgreSQL 17 系 |
| ビルド | Gradle Wrapper 9.3（war パッケージング） |
| CSS | Bulma、Font Awesome（webjar） |

- セッションは Spring Session により PostgreSQL へ保存する。Web サーバを冗長化した際にサーバ間でセッションを共有できる。5.1 の検索条件の保持はこれに依存する
- Entity と Repository の基底クラスは spring-jdbc-codegen で自動生成する
- Spring Security により CSRF 対策と CSP（`default-src 'self'`）を有効化する。認証は行わない
- ErrorProne + NullAway による null 安全チェックをビルド時に行う。**全体を `@NullMarked` とし、`@NullUnmarked` は使わない**。null を許容する箇所はフィールドや引数ごとに `@Nullable` で表す
- Entity はカラムごとに null 許容を表す。spring-jdbc-codegen が生成する。todo テーブルでは `todo_status` と `detail` が非 null、`updated_by` と `created_by` がプリミティブ、`todo_id`・`deadline`・`updated_at`・`created_at` が `@Nullable` になる
- Form（`domain.dto`）は HTTP から受けた**未検証の入力**を保持するため、全フィールドを `@Nullable` とする。送られてこないパラメータは null になるため

### 2.2 パッケージ構成

```
jp.green_code
├── shared              他プロジェクトでも流用しうる汎用コード
│   ├── component
│   ├── config
│   ├── dto
│   └── util
└── todo                このアプリ固有の機能
    ├── domain          presentation 以外
    │   ├── config
    │   ├── dto
    │   ├── entity
    │   │   └── base    自動生成
    │   ├── enums
    │   ├── repository
    │   │   └── base    自動生成
    │   └── service
    └── presentation    HTTP 関連
        ├── config
        ├── controller
        └── interceptor
```

分割の方針は以下のとおり。

| パッケージ | 置くもの |
|---|---|
| `shared` | Todo アプリに依存せず、他プロジェクトでもそのまま使える見込みのコード |
| `todo` | このアプリ固有の機能 |
| `todo.presentation` | HTTP に関わるもの（Controller、Interceptor、Web 設定） |
| `todo.domain` | `presentation` に該当しないもの |

- `shared` は `todo` パッケージを参照しない
- `presentation` と `domain` より下の階層は、一般的な構成に従う

## 3. 画面と URL

| URL | メソッド | テンプレート | 説明 |
|---|---|---|---|
| `/` | GET | － | `/list` へリダイレクト |
| `/list` | GET | `todo/todo_list.html` | 一覧・検索 |
| `/new` | GET | `todo/todo_edit.html` | 登録画面 |
| `/edit/{todoId}` | GET | `todo/todo_edit.html` | 編集画面。該当 ID が存在しない場合は 404 |
| `/upsert` | POST | － | 登録・更新の実行 |

## 4. データモデル

### 4.1 todo テーブル

| カラム | 型 | NULL | 既定値 | 説明 |
|---|---|---|---|---|
| `todo_id` | bigserial | 不可 | 採番 | 主キー。10001 から採番 |
| `todo_status` | `todo_status`（enum 型） | 不可 | `'NEW'` | ステータス。4.2 参照 |
| `detail` | text | 不可 | `''` | やることの内容 |
| `deadline` | timestamptz | 可 | － | 完了予定日 |
| `updated_at` | timestamptz | 不可 | now() | 更新日時 |
| `updated_by` | bigint | 不可 | -1 | 更新者。account への外部キー |
| `created_at` | timestamptz | 不可 | now() | 作成日時 |
| `created_by` | bigint | 不可 | -1 | 作成者。account への外部キー |

`updated_at` / `created_at` はトリガー `refresh_meta_columns()` が設定するため、アプリケーションから指定した値は採用されない。

- INSERT 時：`updated_at`、`created_at` に現在時刻を設定する。`created_by` はトリガーの対象外で、アプリケーションが指定した値がそのまま入る
- UPDATE 時：`updated_at` に現在時刻を設定し、`created_at`・`created_by` は更新前の値を維持する

`created_by` と `updated_by` はアプリケーションが値を入れる（5.2 参照）。
DB の既定値 -1 は使われない。

### 4.2 ステータス（TodoStatusEnum）

| 値 | 表示 |
|---|---|
| `NEW` | 未処理 |
| `DOING` | 処理中 |
| `DONE` | 完了 |

## 5. 機能仕様

### 5.1 一覧・検索（`/list`）

検索条件を指定して todo を絞り込み、ページングして表示する。

**検索条件**

| 項目 | 条件 |
|---|---|
| `word` | `detail` の部分一致 |
| `status` | `todo_status` の完全一致 |
| `deadlineFrom` | `deadlineFrom <= deadline` |
| `deadlineTo` | `deadline <= deadlineTo` |

- `word` に含まれる `%` と `_` はワイルドカードではなくリテラルとして扱う
- 未入力の条件は絞り込みに使用しない
- 複数指定した場合は AND で結合する
- 総件数を取得し、0 件の場合は明細取得の SQL を実行しない

**並び順（`sort` / TodoSearchSortEnum）**

| 値 | 表示 | 並び順 |
|---|---|---|
| `UPDATE_DESC` | 更新日 降順 | `updated_at desc, todo_id desc` |
| `UPDATE_ASC` | 更新日 昇順 | `updated_at asc, todo_id asc` |
| `STATUS` | ステータス | `todo_status desc, todo_id desc` |
| `DEADLINE_DESC` | 完了予定日 降順 | `deadline desc, todo_id desc` |
| `DEADLINE_ASC` | 完了予定日 昇順 | `deadline asc, todo_id asc` |

未指定時は `UPDATE_DESC` を使用する。

**ページング**

- `currentPage`：現在ページ。1 始まり、既定値 1
- `pageSize`：1 ページあたり件数。既定値 10、上限 1000
- ページャは現在ページの前後 2 ページを表示する

**検索条件の保持**

- 検索条件はセッション（キー `prevTodoListCondition`）に保存する
- `usePrevCondition=true` を指定してアクセスした場合、セッションの条件を復元し、`/list` へリダイレクトする。リダイレクトするのは、アドレスバーに `usePrevCondition=true` を残さないため
- 復元時、通知メッセージが FlashAttribute にあれば併せて引き継ぐ

### 5.2 登録・更新（`POST /upsert`）

登録と更新を同一のエンドポイントで扱う。`todoId` が未指定の場合は登録、指定されている場合は更新とする。

**バリデーションエラー時**

1. 通知メッセージ `notification.validation-error` をエラー種別で表示するよう FlashAttribute に設定
2. 入力値とバリデーション結果を FlashAttribute に保存
3. 登録の場合は `/new`、更新の場合は `/edit/{todoId}` へリダイレクトし、入力値とエラー内容を復元して表示する

**成功時**

1. 通知メッセージを FlashAttribute に設定（登録：`notification.complete-post` / 更新：`notification.complete-update`）
2. `/list?usePrevCondition=true` へリダイレクトし、直前の検索条件で一覧に戻る

**登録・更新の内容**

- 登録：`todo_id` を INSERT の対象から外し、DB に採番させる
- `created_by`（登録時のみ）と `updated_by` にダミーアカウント ID -1 を設定する（認証未実装のため）
- 更新時に送った `created_by` はトリガーが更新前の値へ戻すため、値は変わらない
- `todoStatus` の入力欄は更新画面にのみ存在する。新規登録画面には入力欄がなく送信されないため、登録時は `NEW` を設定する

## 6. バリデーション

エラーメッセージは `messages.properties` の `validation.*` を使用する。

Form は全フィールドが `@Nullable` で、未検証の入力をそのまま保持する。
**検証を通った値だけを Entity へ移す。** Entity の非 null なフィールド（`detail` など）へ
未検証の値を渡すとコンパイルエラーになるため、この順序は型で守られる。

### 6.1 登録・更新（TodoDto）

| 項目 | 必須 | 規則 |
|---|---|---|
| `todoId` | 任意 | 0 以上 |
| `detail` | 必須 | 1 文字以上 100 文字以下 |
| `deadline` | 任意 | `y-M-d` 形式（例：`2026-1-5`、`2026-01-05`) |
| `todoStatus` | 任意 | TodoStatusEnum のいずれか |

### 6.2 検索（TodoSearchDto）

| 項目 | 必須 | 規則 |
|---|---|---|
| `word` | 任意 | 100 文字以下 |
| `status` | 任意 | TodoStatusEnum のいずれか |
| `deadlineFrom` | 任意 | `y-M-d` 形式 |
| `deadlineTo` | 任意 | `y-M-d` 形式 |
| `sort` | 任意 | TodoSearchSortEnum のいずれか |

いずれの項目も、未入力の場合は後続の形式チェックを行わずエラーとしない。

### 6.3 日付の扱い

- 画面から受け取った日付文字列は、`app-shared.zone-id`（既定 `Asia/Tokyo`）のタイムゾーンで `OffsetDateTime` に変換して扱う
- 画面へ表示する際は `yyyy-MM-dd` 形式に整形する

## 7. 通知メッセージ

| キー | 表示 | 種別 |
|---|---|---|
| `notification.complete-post` | やることを登録しました | 成功 |
| `notification.complete-update` | やることを更新しました | 成功 |
| `notification.validation-error` | バリデーションエラー | エラー |
