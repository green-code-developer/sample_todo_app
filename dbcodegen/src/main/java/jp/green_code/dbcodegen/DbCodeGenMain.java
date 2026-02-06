package jp.green_code.dbcodegen;

/**
 * Entity Repository 自動生成ツール
 */
public class DbCodeGenMain {
    public static void main(String[] args) throws Exception {
        System.out.println("dbcodegen start");
        var runner = new DbCodeGenRunner();
        runner.run();
        System.out.println("dbcodegen end");
    }
}