package com.couple.space.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * Web配置类
 * 配置跨域请求
 */
@Slf4j
@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(false)
                .maxAge(3600);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HandlerInterceptor() {
            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
                log.info("收到请求: {} {}", request.getMethod(), request.getRequestURI());
                log.info("完整URL: {}", request.getRequestURL());
                log.info("请求参数: {}", request.getQueryString());
                log.info("请求头: Origin={}, Referer={}", 
                    request.getHeader("Origin"), 
                    request.getHeader("Referer"));
                return true;
            }
        }).addPathPatterns("/**");
    }
} 