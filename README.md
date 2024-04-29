# Todo アプリ
## 概要
- 新規プロジェクトの立ち上げ時に使用可能なテンプレートです。
- ローカル環境に必要なものは全てdocker-compose で構築し、できる限り少ないステップで構築と再構築ができます。
- 

## 構成
- Spring Boot
- PostgreSQL
- Bulma CSS
- Font-awesome
- （Javascript ライブラリは使用しない）

## 環境
### 事前インストール
Java17+ とDocker をインストールしておく

- [Java17 Amazon corretto](https://aws.amazon.com/jp/corretto/)
- [Docker Desktop](https://docs.docker.jp/desktop/install.html)
- Windows でDocker 起動時にWSL のエラーが出る場合は[Linux カーネル更新プログラム](https://www.learning-nao.com/?p=3934)もインストールする
- [Docker Compose](https://docs.docker.jp/compose/install.html)

### 構築手順
Mac & Linux 環境の場合
```shell
cd [プロジェクトルートのパス]
make
```

Windows 環境の場合
Makefile に記載されているコマンドと同等の処理を実行してください。（以後同じ）

### アプリ起動
```shell
cd [プロジェクトルートのパス]
make run
```

もしくは、TodoApplication のmain 関数を実行します。

ブラウザからこちらに接続できます
http://localhost:52232


### ビルド手順
```shell
cd [プロジェクトルートのパス]
make jar
```
