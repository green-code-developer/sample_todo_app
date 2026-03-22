package jp.green_code.todo.presentation.config;

import jp.green_code.todo.presentation.interceptor.HeaderInterceptor;
import jp.green_code.todo.presentation.interceptor.MdcInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    public final String[] EXCLUDE_PATTERN = new String[]{"/assets/**", "/webjars/**"};

    private final MdcInterceptor mdcInterceptor;
    private final HeaderInterceptor headerInterceptor;

    public InterceptorConfig(MdcInterceptor mdcInterceptor, HeaderInterceptor headerInterceptor) {
        this.mdcInterceptor = mdcInterceptor;
        this.headerInterceptor = headerInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(headerInterceptor);
        registry.addInterceptor(mdcInterceptor).excludePathPatterns(EXCLUDE_PATTERN);
    }
}
