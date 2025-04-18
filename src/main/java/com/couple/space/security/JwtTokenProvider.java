package com.couple.space.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;

/**
 * JWT令牌提供者
 * 负责JWT令牌的生成、验证和解析
 * 主要功能：
 * 1. 生成JWT令牌
 * 2. 验证JWT令牌
 * 3. 从令牌中提取用户信息
 */
@Component
public class JwtTokenProvider {
    /**
     * JWT签名密钥
     * 用于：
     * - 签名JWT令牌
     * - 验证JWT令牌
     */
    private final Key jwtSigningKey;
    
    /**
     * JWT令牌的有效期（毫秒）
     * 设置为24小时
     * 用于控制令牌的过期时间
     */
    private static final long JWT_EXPIRATION = 86400000;

    /**
     * 构造函数，注入依赖
     * @param jwtSigningKey JWT签名密钥
     */
    public JwtTokenProvider(Key jwtSigningKey) {
        this.jwtSigningKey = jwtSigningKey;
    }

    /**
     * 生成JWT令牌
     * 流程：
     * 1. 从认证信息中获取用户信息
     * 2. 设置令牌的签发时间和过期时间
     * 3. 使用签名密钥生成令牌
     * @param authentication 认证信息
     * @return JWT令牌
     */
    public String generateToken(Authentication authentication) {
        // 从认证信息中获取用户信息
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        // 设置令牌的签发时间和过期时间
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION);

        // 生成JWT令牌
        return Jwts.builder()
                .setSubject(userDetails.getUsername())  // 设置主题（用户名）
                .setIssuedAt(now)  // 设置签发时间
                .setExpiration(expiryDate)  // 设置过期时间
                .signWith(jwtSigningKey)  // 使用签名密钥
                .compact();  // 生成令牌
    }

    /**
     * 从令牌中获取用户名
     * 流程：
     * 1. 解析JWT令牌
     * 2. 获取令牌中的主题（用户名）
     * @param token JWT令牌
     * @return 用户名
     */
    public String getUsernameFromToken(String token) {
        // 解析JWT令牌
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(jwtSigningKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        // 获取令牌中的主题（用户名）
        return claims.getSubject();
    }

    /**
     * 验证JWT令牌
     * 流程：
     * 1. 尝试解析JWT令牌
     * 2. 如果解析成功，说明令牌有效
     * 3. 如果解析失败，说明令牌无效
     * @param token JWT令牌
     * @return 是否有效
     */
    public boolean validateToken(String token) {
        try {
            // 尝试解析JWT令牌
            Jwts.parserBuilder()
                    .setSigningKey(jwtSigningKey)
                    .build()
                    .parseClaimsJws(token);
            return true;  // 令牌有效
        } catch (JwtException | IllegalArgumentException e) {
            return false;  // 令牌无效
        }
    }
} 