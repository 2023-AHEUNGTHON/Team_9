package com.todayMohang.likelion.todayMohang.jwt;

import com.todayMohang.likelion.todayMohang.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {  
    private final UserService userService;
    private final String secretKey;

    @Override  //권한부여
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authorization=request.getHeader(HttpHeaders.AUTHORIZATION); 
        log.info("authorization : {}", authorization);

        // 토큰 입력 여부
        if(authorization==null || !authorization.startsWith("Bearer ")){
            log.error("authorization을 잘못보냈습니다.");
            filterChain.doFilter(request, response);
            return;
        }
        // 토큰꺼내기
        String token=authorization.split(" ")[1];  //authorization에서 첫공백 이전까지가 토큰

        // 토큰 Expired 여부
        if(JwtUtil.isExpired(token, secretKey)){
            log.error("토큰이 만료되었습니다.");
            filterChain.doFilter(request, response);
            return;
        }
        String email=JwtUtil.getEmail(token, secretKey);
        log.info("email: {}", email);

        UsernamePasswordAuthenticationToken authenticationToken=
                new UsernamePasswordAuthenticationToken(email, null, List.of(new
                        SimpleGrantedAuthority("USER")));
        // 디테일
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }
}