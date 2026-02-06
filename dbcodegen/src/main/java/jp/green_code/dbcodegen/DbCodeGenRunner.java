package jp.green_code.dbcodegen;

import jp.green_code.dbcodegen.db.DbDefinitionReader;
import jp.green_code.dbcodegen.db.DbTypeMapper;
import jp.green_code.dbcodegen.db.JavaType;
import jp.green_code.dbcodegen.db.TableDefinition;
import jp.green_code.dbcodegen.generator.BaseEntityGenerator;
import jp.green_code.dbcodegen.generator.BaseRepositoryGenerator;
import jp.green_code.dbcodegen.generator.EntityGenerator;
import jp.green_code.dbcodegen.generator.HelperGenerator;
import jp.green_code.dbcodegen.generator.RepositoryGenerator;
import jp.green_code.dbcodegen.generator.TestBaseRepositoryGenerator;
import jp.green_code.dbcodegen.generator.TestRepositoryGenerator;
import org.apache.commons.io.FileUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import static jp.green_code.dbcodegen.DbCodeGenParameter.param;
import static jp.green_code.dbcodegen.DbCodeGenUtil.dumpTableDefinitions;

public class DbCodeGenRunner {

    public void run() throws Exception {
        param = readParameter();
        var dbDefinitionReader = new DbDefinitionReader();
        appendEnum();
        var tables = dbDefinitionReader.readDefinition();
        dumpTableDefinitions(tables);
        deleteBaseSources();
        for (var t : tables) {
            writeEntity(t);
        }
        writeHelper();
        for (var t : tables) {
            writeRepository(t);
        }
        for (var t : tables) {
            writeTestRepository(t);
        }
    }

    static DbCodeGenParameter readParameter() throws IOException {
        Yaml yaml = new Yaml();
        try (InputStream is = DbCodeGenMain.class.getClassLoader().getResourceAsStream("param.yml")) {
            return yaml.loadAs(is, DbCodeGenParameter.class);
        }
    }

    void appendEnum() {
        param.enumJavaTypeMappings.forEach((key, value) -> {
            var javaType = new JavaType(value, ":%s::" + key);
            DbTypeMapper.put(key, javaType);
        });
    }

    void deleteBaseSources() throws IOException {
        Path entityBaseDir = Path.of(param.mainJavaDir, param.baseEntityPackage().replace(".", "/"));
        FileUtils.deleteDirectory(entityBaseDir.toFile());
        Path repositoryBaseDir = Path.of(param.mainJavaDir, param.baseRepositoryPackage().replace(".", "/"));
        FileUtils.deleteDirectory(repositoryBaseDir.toFile());
        Path testRepositoryBaseDir = Path.of(param.testJavaDir, param.baseRepositoryPackage().replace(".", "/"));
        FileUtils.deleteDirectory(testRepositoryBaseDir.toFile());
    }

    void writeEntity(TableDefinition tableDef) throws IOException {
        var baseGenerator = new BaseEntityGenerator(param);
        var baseCode = baseGenerator.generateBaseEntityCode(tableDef);
        writeJavaCode(param.mainJavaDir, param.baseEntityPackage(), tableDef.toBaseEntityClassName(), baseCode);

        var generator = new EntityGenerator(param);
        var code = generator.generateEntityCode(tableDef);
        writeJavaCodeIfAbsent(param.mainJavaDir, param.entityPackage, tableDef.toEntityClassName(), code);
    }

    void writeJavaCode(String dir, String packageName, String className, String code) throws IOException {
        var packagePath = packageName.replace(".", "/");
        Path file = Path.of(dir, packagePath, "%s.java".formatted(className));
        Files.createDirectories(file.getParent());
        Files.writeString(file, code, CREATE, TRUNCATE_EXISTING);
    }

    void writeJavaCodeIfAbsent(String dir, String packageName, String className, String code) throws IOException {
        var packagePath = packageName.replace(".", "/");
        Path file = Path.of(dir, packagePath, "%s.java".formatted(className));
        if (!param.forceOverwriteImplementation && Files.exists(file)) {
            // ファイルがあれば何もしない
            return;
        }
        Files.createDirectories(file.getParent());
        Files.writeString(file, code, CREATE, TRUNCATE_EXISTING);
    }

    void writeHelper() throws IOException {
        var generator = new HelperGenerator(param);
        var code = generator.generateHelper();
        writeJavaCode(param.mainJavaDir, param.baseRepositoryPackage(), param.repositoryHelperClassName, code);
    }

    void writeRepository(TableDefinition table) throws IOException {
        var baseGenerator = new BaseRepositoryGenerator(param, table);
        var baseCode = baseGenerator.generateBaseRepositoryCode();
        writeJavaCode(param.mainJavaDir, param.baseRepositoryPackage(), table.toBaseRepositoryClassName(), baseCode);

        var generator = new RepositoryGenerator(param, table);
        var normalCode = generator.generateRepositoryCode();
        writeJavaCodeIfAbsent(param.mainJavaDir, param.repositoryPackage, table.toRepositoryClassName(), normalCode);
    }

    void writeTestRepository(TableDefinition table) throws IOException {
        if (table.shouldSkipTableInTest()) {
            return;
        }
        if (table.pkColumns().isEmpty()) {
            System.out.printf("table:%s without pk skipped test code%n", table.tableName);
            return;
        }
        var testBaseGenerator = new TestBaseRepositoryGenerator(param, table);
        var testBaseCode = testBaseGenerator.generateBaseTestCode();
        writeJavaCode(param.testJavaDir, param.baseRepositoryPackage(), table.toTestBaseRepositoryClassName(), testBaseCode);

        var testGenerator = new TestRepositoryGenerator(param, table);
        var testCode = testGenerator.generateTestRepositoryCode();
        writeJavaCodeIfAbsent(param.testJavaDir, param.repositoryPackage, table.toTestRepositoryClassName(), testCode);
    }
}
