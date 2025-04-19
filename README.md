# Todo アプリ
## 概要
- 新規プロジェクト立ち上げ時に便利なテンプレートプロジェクトです
- 簡単なTODO アプリとなっており、ブラウザから検索、登録、更新を行うことができます
- ローカル環境に必要なデータベース(Postgres) をdocker-compose で構築します
- 認証は実装していません（シングルサインオンを推奨します）

## デモ
https://todo.green-code.jp

## 構成
- Spring Boot
- PostgreSQL
- Bulma CSS
- Font-awesome

## 環境
### 事前インストール
Java21+ と Docker をインストールします

- [Java21 Amazon corretto](https://aws.amazon.com/jp/corretto/)
- [Docker Desktop](https://docs.docker.jp/desktop/install.html)
- Windows はDocker 起動時にWSL のエラーが出る場合ので[Linux カーネル更新プログラム](https://www.learning-nao.com/?p=3934)もインストールしておく

### 構築手順
Mac & Linux 環境の場合
```shell
cd [プロジェクトルートのパス]
make
```

Windows 環境の場合
```shell
cd [プロジェクトルートのパス]
cd docker_todo
docker compose up -d
```

## 起動
Mac & Linux 環境の場合
```shell
cd [プロジェクトルートのパス]
make run
```

Windows 環境の場合
```shell
cd [プロジェクトルートのパス]
.\gradlew.bat bootRun
```

ブラウザからこちらに接続できます

http://localhost:56480
