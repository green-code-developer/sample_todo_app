SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
cd "$SCRIPT_DIR" || exit 1
[ ! -f sjc.jar ] &&curl -L -o sjc.jar https://github.com/green-code-developer/spring-jdbc-codegen/releases/download/v2.0/spring_jdbc_codegen-2.0.jar
java -jar sjc.jar param.yml
