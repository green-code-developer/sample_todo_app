SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
cd "$SCRIPT_DIR" || exit 1
java -jar spring_jdbc_codegen-1.0.jar param.yml
