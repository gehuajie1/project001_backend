package com.couple.space.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * JWT配置类
 * 配置JWT相关的设置，包括：
 * 1. 签名密钥
 * 2. 令牌过期时间
 * 3. 签名算法
 * 4. 令牌生成和验证
 */
@Configuration
public class JwtConfig {
    /**
     * JWT签名密钥
     * 使用HMAC-SHA256算法生成
     * 特点：
     * 1. 安全性高
     * 2. 性能好
     * 3. 适合对称加密
     * @return 签名密钥
     */
    @Bean
    public Key jwtSigningKey() {
        return Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }

    /**
     * JWT令牌的有效期（毫秒）
     * 设置为24小时
     * 用于控制令牌的过期时间
     */
    private static final long JWT_EXPIRATION = 86400000;

    /**
     * JWT签名密钥
     * 使用一个足够长的随机字符串作为密钥
     * 注意：在实际生产环境中，应该从配置文件中读取
     */
    private static final String SECRET_KEY = "your-secret-key-must-be-at-least-256-bits-long-for-HS256";

    /**
     * 获取签名密钥
     * @return 用于JWT签名的密钥
     */
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    /**
     * 为用户生成JWT令牌
     * @param userDetails 用户详细信息
     * @return 生成的JWT令牌字符串
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    /**
     * 创建JWT令牌
     * @param claims 令牌中要包含的额外信息
     * @param subject 令牌的主题（通常是用户名）
     * @return 生成的JWT令牌字符串
     */
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)  // 设置自定义信息
                .setSubject(subject)  // 设置主题
                .setIssuedAt(new Date(System.currentTimeMillis()))  // 设置签发时间
                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION))  // 设置过期时间
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)  // 使用密钥签名
                .compact();  // 生成最终的令牌字符串
    }

    /**
     * 验证JWT令牌是否有效
     * @param token JWT令牌
     * @param userDetails 用户详细信息
     * @return 如果令牌有效返回true，否则返回false
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /**
     * 从JWT令牌中提取用户名
     * @param token JWT令牌
     * @return 令牌中包含的用户名
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * 从JWT令牌中提取过期时间
     * @param token JWT令牌
     * @return 令牌的过期时间
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * 从JWT令牌中提取指定的信息
     * @param token JWT令牌
     * @param claimsResolver 用于提取特定信息的函数
     * @return 提取的信息
     */
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * 从JWT令牌中提取所有信息
     * @param token JWT令牌
     * @return 包含所有信息的Claims对象
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 检查JWT令牌是否已过期
     * @param token JWT令牌
     * @return 如果令牌已过期返回true，否则返回false
     */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
} 