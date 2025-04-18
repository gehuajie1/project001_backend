package com.couple.space.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JWT认证过滤器
 * 用于处理JWT令牌的认证
 * 继承OncePerRequestFilter确保每个请求只被过滤一次
 * 主要功能：
 * 1. 从请求头中提取JWT令牌
 * 2. 验证JWT令牌
 * 3. 设置认证信息
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    /**
     * JWT令牌提供者
     * 用于：
     * - 验证JWT令牌
     * - 从令牌中提取用户信息
     */
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    
    /**
     * 用户详情服务
     * 用于：
     * - 加载用户信息
     * - 构建认证对象
     */
    @Autowired
    private CustomUserDetailsService userDetailsService;

    /**
     * 执行过滤
     * 流程：
     * 1. 从请求头中获取JWT令牌
     * 2. 验证令牌
     * 3. 从令牌中获取用户名
     * 4. 加载用户信息
     * 5. 设置认证信息
     * @param request HTTP请求
     * @param response HTTP响应
     * @param filterChain 过滤器链
     * @throws ServletException Servlet异常
     * @throws IOException IO异常
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // 如果是登录或注册接口，直接放行
        if (request.getRequestURI().equals("/api/users/login") || 
            request.getRequestURI().equals("/api/users/register")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // 从请求头中获取JWT令牌
            String jwt = getJwtFromRequest(request);

            if (jwt != null && jwtTokenProvider.validateToken(jwt)) {
                // 从令牌中获取用户名
                String username = jwtTokenProvider.getUsernameFromToken(jwt);
                // 加载用户信息
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                // 创建认证令牌
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                // 设置认证详情
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // 设置认证信息
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception ex) {
            logger.error("无法设置用户认证", ex);
        }

        // 继续过滤器链
        filterChain.doFilter(request, response);
    }

    /**
     * 从请求头中获取JWT令牌
     * 流程：
     * 1. 从Authorization请求头中获取令牌
     * 2. 如果令牌存在且以"Bearer "开头，则提取令牌
     * @param request HTTP请求
     * @return JWT令牌
     */
    private String getJwtFromRequest(HttpServletRequest request) {
        // 从Authorization请求头中获取令牌
        String bearerToken = request.getHeader("Authorization");
        // 如果令牌存在且以"Bearer "开头，则提取令牌
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
} 