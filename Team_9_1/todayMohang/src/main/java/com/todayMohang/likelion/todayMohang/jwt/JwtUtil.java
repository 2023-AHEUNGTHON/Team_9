package com.todayMohang.likelion.todayMohang.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.nio.charset.StandardCharsets;
import java.util.Date;

public class JwtUtil {

    // 토큰 생성
    public static String createJwt(String email, String secretKey, Long expiredMs){
        Claims claims= Jwts.claims();
        claims.put("email", email); 
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes(StandardCharsets.UTF_8))
                .compact();
    }

    private static byte[] getSecretKeyBytes(String secretKey){
        return secretKey.getBytes(StandardCharsets.UTF_8);
    }

    // 토큰 유효기간 만료 여부
    public static boolean isExpired(String token, String secretKey) {
        byte[] secretKeyBytes = getSecretKeyBytes(secretKey);
        return Jwts.parser().setSigningKey(secretKeyBytes).parseClaimsJws(token).getBody()
                .getExpiration().before(new Date());
    }

    // 토큰에서 사용자 email 가져오기
    public static String getEmail(String token, String secretKey) {
        byte[] secretKeyBytes = getSecretKeyBytes(secretKey);
        return Jwts.parser().setSigningKey(secretKeyBytes).parseClaimsJws(token)
                .getBody().get("email", String.class);
    }
}