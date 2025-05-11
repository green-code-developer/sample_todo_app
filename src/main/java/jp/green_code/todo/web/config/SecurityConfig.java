package jp.green_code.todo.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

/**
 * Spring Security 設定
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final String CONTENT_SECURITY_POLICY = "default-src 'self'";

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // デモアプリではユーザ認証を行わない
        http.securityMatcher("/**")
            .authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll());

        // CSP 有効化
        http.headers((headers) -> headers.contentSecurityPolicy(
            config -> config.policyDirectives(CONTENT_SECURITY_POLICY)));

        // csrf 有効化
        http.csrf(csrf -> csrf
            .csrfTokenRepository(new HttpSessionCsrfTokenRepository())
        );

        return http.build();
    }
}