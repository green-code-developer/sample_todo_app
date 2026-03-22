package jp.green_code.todo.presentation.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Objects;

@Component
public class MdcInterceptor implements HandlerInterceptor {

    public static final String MDC_KEY_ACCOUNT_ID = "ACCOUNT_ID";

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        MDC.clear();
        // ログイン機能を持っていないため固定値をセット
        var accountId = 10001L;
        MDC.put(MDC_KEY_ACCOUNT_ID, Objects.toString(accountId, ""));
        return true;
    }
}
