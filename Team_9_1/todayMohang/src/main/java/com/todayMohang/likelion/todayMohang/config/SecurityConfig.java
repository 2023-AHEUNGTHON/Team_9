package com.todayMohang.likelion.todayMohang.config;

import com.todayMohang.likelion.todayMohang.jwt.JwtFilter;
import com.todayMohang.likelion.todayMohang.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final UserService userService;

    @Value("${jwt.secret}")
    private String secretKey;

    public SecurityConfig(UserService userService) { //애플리케이션이 시작될 때 자동으로 생성되고 적용됨
        this.userService = userService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // 모든 요청에 대해 접근 허용
                .authorizeRequests() // url 접근 권한 설정
                .antMatchers("/user/**", "/swagger-ui/**","/webjars/**",
                        "/v2/api-docs","/swagger-resources/**", "/posts", "/post/**").permitAll() // user 관련 url 모두 허용
                .anyRequest().authenticated() // 그 외 요청은 인증 필요
                .and()
                // 인증이 필요한 url(요청)은 JWT필터를 통과해야 함
                .addFilterBefore(new JwtFilter(userService, secretKey), UsernamePasswordAuthenticationFilter.class)
                // CORS 설정
                .cors().configurationSource(corsConfigurationSource())
                .and()
                // 세션 사용 안함
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        return http.build();
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    private CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);  // 자격 증명 정보 허용
        config.addAllowedOriginPattern("*"); // 모든 출저에서 요청을 허용
        config.addAllowedHeader("*"); // 모든 헤더 허용
        config.addAllowedMethod("*"); // 모든 HTTP 메소드 허용
        source.registerCorsConfiguration("/**", config);  // 모든 URL패턴에 CORS설정 적용
        return source;
    }
}
