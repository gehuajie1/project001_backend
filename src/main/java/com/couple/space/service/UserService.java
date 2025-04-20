package com.couple.space.service;

import com.couple.space.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.util.Optional;

/**
 * 用户服务接口
 * 定义用户相关的业务逻辑方法，包括：
 * 1. 用户注册
 * 2. 用户登录
 * 3. 用户认证
 */
public interface UserService extends UserDetailsService {
    /**
     * 注册新用户
     * @param user 要注册的用户对象
     * @return 注册成功的用户对象
     * @throws RuntimeException 如果用户名已存在
     */
    User register(User user);

    /**
     * 检查用户名是否存在
     * @param username 用户名
     * @return 1表示存在，0表示不存在
     */
    int existsByUsername(String username);

    /**
     * 用户登录
     * @param username 用户名
     * @param encryptedPassword 前端加密后的密码
     * @return 登录成功的用户信息，如果登录失败返回空
     */
    Optional<User> login(String username, String encryptedPassword);

    /**
     * 更新所有用户的密码
     * @param newPassword 新的密码（前端已经加密）
     * @return 更新的用户数量
     */
    int updateAllUserPasswords(String newPassword);
} 