package com.couple.space.security;

import com.couple.space.entity.User;
import com.couple.space.mapper.UserMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 自定义用户详情服务
 * 实现 UserDetailsService 接口，用于加载用户信息
 * 主要功能：
 * 1. 根据用户名加载用户信息
 * 2. 将用户信息转换为 UserDetails 对象
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    // 用户数据访问接口，用于从数据库查询用户信息
    private final UserMapper userMapper;

    /**
     * 构造函数，注入依赖
     * @param userMapper 用户数据访问接口
     */
    public CustomUserDetailsService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    /**
     * 根据用户名加载用户信息
     * 流程：
     * 1. 从数据库中查询用户信息
     * 2. 如果用户不存在，抛出 UsernameNotFoundException
     * 3. 如果用户存在，将用户信息转换为 UserDetails 对象
     * @param username 用户名
     * @return 用户详情
     * @throws UsernameNotFoundException 用户不存在
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 从数据库中查询用户信息
        User user = userMapper.findByUsername(username);
        
        // 如果用户不存在，抛出 UsernameNotFoundException
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在: " + username);
        }
        
        // 将用户信息转换为 UserDetails 对象
        // 使用 Spring Security 提供的 User 构建器创建 UserDetails 对象
        // 设置用户名、密码和权限
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())  // 设置用户名
                .password(user.getPassword())      // 设置密码
                .authorities("ROLE_USER")          // 设置用户角色
                .build();                          // 构建 UserDetails 对象
    }
} 