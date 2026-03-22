package jp.green_code.todo.presentation.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class HeaderInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        // Apache やロードバランサなどのレスポンスと区別できるように固定値を入れておく
        response.setHeader("X-Server", "SpringBoot(todo)");
        return true;
    }
}
