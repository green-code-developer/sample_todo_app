package jp.green_code.todo.todo.web.interceptor;

import static jp.green_code.todo.todo.util.AppConstants.MDC_REQUEST_UUID;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.UUID;
import org.slf4j.MDC;
import org.springframework.web.servlet.HandlerInterceptor;

public class MdcInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(
        HttpServletRequest request, HttpServletResponse response, Object handler
    ) {
        UUID uniqueId = UUID.randomUUID();
        MDC.clear();
        MDC.put(MDC_REQUEST_UUID, uniqueId.toString());
        return true;
    }
}
