package com.couple.space.service.impl;

import com.couple.space.entity.User;
import com.couple.space.mapper.UserMapper;
import com.couple.space.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

/**
 * 用户服务实现类
 * 实现UserService接口定义的所有方法
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;

    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在: " + username);
        }
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities("USER")
                .build();
    }

    @Override
    @Transactional
    public User register(User user) {
        if (userMapper.existsByUsername(user.getUsername()) > 0) {
            throw new RuntimeException("用户名已存在");
        }
        
        user.setCreatedAt(java.time.LocalDateTime.now());
        user.setUpdatedAt(java.time.LocalDateTime.now());
        
        // log.info("用户注册 - 用户名: {}, 加密后密码: {}", 
        //     user.getUsername(), user.getPassword());
        
        userMapper.insert(user);
        return user;
    }

    @Override
    public Optional<User> login(String username, String encryptedPassword) {
        // log.info("尝试登录用户: {}", username);
        try {
            User user = userMapper.findByUsername(username);
            // log.info("查询用户结果: {}", user != null ? "找到用户" : "用户不存在");
            
            if (user != null) {
                // log.info("开始验证密码 - 前端加密密码: {}, 数据库密码: {}", encryptedPassword, user.getPassword());
                // 直接比较前端传来的加密密码和数据库中存储的密码
                boolean passwordMatches = encryptedPassword.equals(user.getPassword());
                // log.info("密码验证结果: {}", passwordMatches ? "成功" : "失败");
                
                if (passwordMatches) {
                    // log.info("用户 {} 登录成功", username);
                    return Optional.of(user);
                } else {
                    // log.warn("用户 {} 密码验证失败", username);
                }
            } else {
                // log.warn("用户 {} 不存在", username);
            }
        } catch (Exception e) {
            // log.error("用户 {} 登录过程中发生错误: {}", username, e.getMessage(), e);
            throw e;
        }
        
        return Optional.empty();
    }

    @Override
    public int existsByUsername(String username) {
        return userMapper.existsByUsername(username);
    }

    @Override
    @Transactional
    public int updateAllUserPasswords(String newPassword) {
        // log.info("开始更新所有用户的密码");
        // 获取所有用户
        List<User> users = userMapper.findAll();
        int updatedCount = 0;
        
        for (User user : users) {
            try {
                // 更新用户密码（直接使用前端加密后的密码）
                user.setPassword(newPassword);
                user.setUpdatedAt(java.time.LocalDateTime.now());
                userMapper.update(user);
                updatedCount++;
                
                // log.info("已更新用户 {} 的密码", user.getUsername());
            } catch (Exception e) {
                // log.error("更新用户 {} 密码失败: {}", user.getUsername(), e.getMessage());
            }
        }
        
        // log.info("密码更新完成，共更新 {} 个用户", updatedCount);
        return updatedCount;
    }
} 