#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
cd "$SCRIPT_DIR" || exit 1
VERSION=4.0
JAR="spring_jdbc_codegen-$VERSION.jar"
[ -f "$JAR" ] || curl -fL -o "$JAR" "https://github.com/green-code-developer/spring-jdbc-codegen/releases/download/v$VERSION/$JAR"
java -jar "$JAR" param.yml
