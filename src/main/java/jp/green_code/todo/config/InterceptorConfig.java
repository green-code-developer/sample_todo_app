package jp.green_code.todo.config;

import jp.green_code.todo.web.interceptor.MdcInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

  @Autowired private LocaleChangeInterceptor localeChangeInterceptor;
  @Autowired private MdcInterceptor mdcInterceptor;

  public final String[] EXCLUDE_PATTERN = new String[] {"/assets/**", "/webjars/**"};

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(localeChangeInterceptor).excludePathPatterns(EXCLUDE_PATTERN);
    registry.addInterceptor(mdcInterceptor).excludePathPatterns(EXCLUDE_PATTERN);
  }
}
