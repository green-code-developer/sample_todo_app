package jp.green_code.dbcodegen;

import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;

/**
 * Entity Repository 自動生成ツール
 */
public class DbCodeGenMain {
    public static void main(String[] args) throws Exception {
        System.out.println("dbcodegen start");
        var param = readParameter();
        var runner = new DbCodeGenRunner(param);
        runner.run();
        System.out.println("dbcodegen end");
    }

    static DbCodeGenParameter readParameter() throws IOException {
        Yaml yaml = new Yaml();
        try (InputStream is = DbCodeGenMain.class.getClassLoader().getResourceAsStream("param.yml")) {
            return yaml.loadAs(is, DbCodeGenParameter.class);
        }
    }
}