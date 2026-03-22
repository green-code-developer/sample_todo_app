# Todo アプリ

## 1. 概要

- Spring Boot + PostgreSQL + Thymeleaf を用いた、すぐに開発を始められるテンプレートプロジェクト
- 簡単なTodo アプリで、ブラウザから検索、登録、更新を行うことができる
- バリデーション・DB・画面構成の基本が実装されている
- ローカル環境に必要なデータベース(PostgreSQL) はdocker compose で構築
- ErrorProne + NullAway によるチェック環境を実現

### 1.2 できないこと

- 認証は実装なし（デモ画面の操作を容易にするため）
- lombok は導入しない（Java 標準record を多用）

## 1.3 構成

- Spring Boot 4系 (Thymeleaf)
- PostgreSQL 17系
- Gradle Wrapper 9.3
- [ErrorProne](https://errorprone.info) + [NullAway](https://github.com/uber/nullaway)
- Entity とRepository は[spring-jdbc-codegen](https://github.com/green-code-developer/spring-jdbc-codegen) で自動作成

### 1.4 デモ

以下の URL から動作確認できます
https://todo.green-code.jp

## 2. セットアップ

### 2.2 事前インストール

Java21+ と Docker のインストールが必要です

- [Java21 Amazon corretto](https://aws.amazon.com/jp/corretto/)
- [Docker Desktop](https://docs.docker.jp/desktop/install.html)

### 2.3 構築手順

```shell
cd [プロジェクトルートのパス]
cd docker_todo
docker compose up -d
```

### 2.4 起動

```shell
cd [プロジェクトルートのパス]
./gradlew bootRun
```

※ Mac/Linux の場合は make && make run コマンドでも実行できます

以下の URL にアクセスすると Todo 一覧画面が表示されます

http://localhost:56480

## 3. 補足

### 3.1 パッケージ構成

- jp.green_code
    - todo
        - domain (ビジネスロジック・エンティティ・リポジトリ)
        - presentation (Controller・Web関連)
    - shared (共通ユーティリティ・プロジェクト横断的)

### 3.2 ErrorProne + NullAway

ErrorProne + NullAway を適用しているため、ビルド時にチェックが入ります。
全体的に@NullMarked を適用し、NulllPointerException を回避する方針です。
例外として、Entity (domain.entity) とForm (domain.dto) はnull が入る可能性があり実装が複雑になるため@NullUnmarked としています。

## 変更履歴

| 日付        | 変更内容                                                                                                                                                                                  |
|-----------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| 2026/3/22 | - パッケージ最上位を見直し<br>- バリデーションをValidationUtil として共通化<br>- lombok 廃止<br>- 可能な限りrecord を導入<br>- ZoneId をapplication.yml で設定する<br>- ログフォーマットを見直してkey=value を基本とし、時刻をapache/nginx と似せて大括弧で囲う 
| 2026/2/2  | - Entity Repository 自動生成ツール dbcodegen を導入(jooq 廃止)                                                                                                                                    
