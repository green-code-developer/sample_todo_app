package jp.green_code.shared.util;

public class ThreadLocalUtil {
    public static String methodName() {
        return Thread.currentThread().getStackTrace()[2].getMethodName();
    }
}
