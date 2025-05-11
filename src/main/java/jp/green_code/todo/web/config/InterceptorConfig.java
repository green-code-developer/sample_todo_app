package jp.green_code.todo.web.config;

import jp.green_code.todo.web.interceptor.HeaderInterceptor;
import jp.green_code.todo.web.interceptor.MdcInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class InterceptorConfig implements WebMvcConfigurer {

    private final MdcInterceptor mdcInterceptor;
    private final HeaderInterceptor headerInterceptor;

    public final String[] EXCLUDE_PATTERN = new String[]{"/assets/**", "/webjars/**"};

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(headerInterceptor);
        registry.addInterceptor(mdcInterceptor).excludePathPatterns(EXCLUDE_PATTERN);
    }
}
