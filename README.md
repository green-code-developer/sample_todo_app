# Todo アプリ
## 概要
- 新規プロジェクト立ち上げ時に便利なテンプレートプロジェクトです
- 簡単なTODO アプリとなっており、ブラウザから検索、登録、更新を行うことができます
- ローカル環境に必要なデータベース(Postgres) をdocker-compose で構築します
- 認証は実装していません（シングルサインオンを推奨します）

## 構成
- Spring Boot
- PostgreSQL
- Bulma CSS
- Font-awesome

## 環境
### 事前インストール
Java17+, Docker, Docker Compose をインストールします

- [Java17 Amazon corretto](https://aws.amazon.com/jp/corretto/)
- [Docker Desktop](https://docs.docker.jp/desktop/install.html)
- Windows はDocker 起動時にWSL のエラーが出る場合ので[Linux カーネル更新プログラム](https://www.learning-nao.com/?p=3934)もインストールしておく
- [Docker Compose](https://docs.docker.jp/compose/install.html)

### 構築手順
Mac & Linux 環境の場合
```shell
cd [プロジェクトルートのパス]
make
```

Windows 環境の場合
```shell
cd [プロジェクトルートのパス]
docker-compose up -d
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

http://localhost:52580

デバッグ時はIDE でTodoApplication のmain 関数を実行してください。
