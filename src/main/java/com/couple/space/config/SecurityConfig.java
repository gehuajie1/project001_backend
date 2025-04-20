package com.couple.space.config;

import com.couple.space.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.http.HttpMethod;

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
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("*"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(false);
        configuration.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
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
            .cors().configurationSource(corsConfigurationSource())  // 配置跨域
            .and()
            .csrf().disable()  // 禁用CSRF保护
            .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()  // 允许所有OPTIONS请求
                .antMatchers("/api/users/register", "/api/users/login").permitAll()  // 允许注册和登录
                .antMatchers("/api/weather/**").permitAll()  // 允许天气接口的访问
                .antMatchers("/api/anniversary/**").permitAll()  // 允许纪念日接口的访问
                .anyRequest().authenticated()  // 其他请求需要认证
            .and()
            .httpBasic().disable()  // 禁用HTTP Basic认证
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)  // 禁用会话管理
            .and()
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }
} 