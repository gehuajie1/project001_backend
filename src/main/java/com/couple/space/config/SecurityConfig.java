package com.couple.space.config;

import com.couple.space.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * Spring Security配置类
 * 配置安全相关的设置，包括：
 * 1. 密码编码器
 * 2. 请求授权规则
 * 3. CSRF保护
 * 4. 跨域设置
 * 5. 会话管理
 * 6. JWT认证过滤器
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    /**
     * JWT认证过滤器
     * 用于处理JWT令牌的认证
     */
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * 构造函数，注入依赖
     * @param jwtAuthenticationFilter JWT认证过滤器
     */
    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    /**
     * 配置密码编码器
     * 使用BCrypt算法进行密码加密
     * BCrypt特点：
     * 1. 自动加盐
     * 2. 可配置的加密强度
     * 3. 抗彩虹表攻击
     * @return BCryptPasswordEncoder实例
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 配置跨域设置
     * 允许：
     * 1. 所有来源的请求
     * 2. 所有HTTP方法
     * 3. 所有请求头
     * 4. 携带凭证
     * @return 跨域配置源
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));  // 允许所有来源
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));  // 允许所有方法
        configuration.setAllowedHeaders(Arrays.asList("*"));  // 允许所有请求头
        configuration.setAllowCredentials(true);  // 允许携带凭证
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);  // 对所有路径生效
        return source;
    }

    /**
     * 配置HTTP安全
     * 设置：
     * 1. 禁用CSRF保护
     * 2. 允许所有请求通过
     * 3. 禁用HTTP Basic认证
     * 4. 配置跨域
     * 5. 禁用会话管理
     * 6. 添加JWT认证过滤器
     * @param http HttpSecurity对象
     * @throws Exception 配置异常
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()  // 禁用CSRF保护
            .cors().configurationSource(corsConfigurationSource())  // 配置跨域
            .and()
            .authorizeRequests()
                .antMatchers("/api/users/register", "/api/users/login").permitAll()  // 允许注册和登录
                .anyRequest().authenticated()  // 其他请求需要认证
            .and()
            .httpBasic().disable()  // 禁用HTTP Basic认证
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)  // 禁用会话管理
            .and()
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }
} 