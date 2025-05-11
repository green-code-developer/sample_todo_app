package jp.green_code.todo.util;

import org.slf4j.MDC;

import java.util.Optional;

import static jp.green_code.todo.web.interceptor.MdcInterceptor.MDC_KEY_ACCOUNT_ID;

public class ThreadLocalUtil {
    public static Optional<Long> getAccountId() {
        var mdcAccountId = MDC.get(MDC_KEY_ACCOUNT_ID);
        return AppNumberUtil.toLong(mdcAccountId);
    }

    public static void setAccountId(long accountId) {
        MDC.put(MDC_KEY_ACCOUNT_ID, accountId + "");
    }

    public static String methodName() {
        return Thread.currentThread().getStackTrace()[2].getMethodName();
    }
}
