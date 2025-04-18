package com.couple.space.mapper;

import com.couple.space.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户数据访问接口
 * 定义与users表相关的数据库操作
 * 使用MyBatis注解和XML映射文件实现
 */
@Mapper
public interface UserMapper {
    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return 用户对象，如果不存在返回null
     */
    User findByUsername(String username);

    /**
     * 检查用户名是否存在
     * @param username 用户名
     * @return 1表示存在，0表示不存在
     */
    int existsByUsername(String username);

    /**
     * 插入新用户
     * @param user 用户对象
     * @return 影响的行数
     */
    int insert(User user);

    /**
     * 更新用户信息
     * @param user 用户对象
     * @return 影响的行数
     */
    int update(User user);
} 