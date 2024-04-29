package com.example.todo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /* content-security-policy
     * 基本的には自ホスト以外許容しない
     * 開発環境や本番環境のドメインが決定後に追加予定
     */
    private static final String CONTENT_SECURITY_POLICY = "default-src 'self'";

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // デモアプリではユーザ認証を行わない
        http.securityMatcher("/**")
            .authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll());

        http.headers((headers) -> headers.contentSecurityPolicy(
            config -> config.policyDirectives(CONTENT_SECURITY_POLICY)));

        return http.build();
    }
}