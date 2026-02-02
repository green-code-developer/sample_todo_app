package jp.green_code.todo.web.interceptor;

import static jp.green_code.todo.util.AppConstants.SYSTEM_ACCOUNT_ID;
import static jp.green_code.todo.util.ThreadLocalUtil.setAccountId;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jp.green_code.todo.entity.AccountEntity;
import jp.green_code.todo.util.SessionUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class MdcInterceptor implements HandlerInterceptor {

    public static final String MDC_KEY_ACCOUNT_ID = "ACCOUNT_ID";

    @Autowired
    private final SessionUtil sessionUtil;

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        MDC.clear();
        var accountId = sessionUtil.getLoggedAccount().map(AccountEntity::getAccountId).orElse(SYSTEM_ACCOUNT_ID);
        setAccountId(accountId);
        return true;
    }
}
