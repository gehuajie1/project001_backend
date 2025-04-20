package com.couple.space.mapper;

import com.couple.space.entity.Anniversary;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

/**
 * 纪念日数据访问接口
 * 定义与anniversaries表相关的数据库操作
 */
@Mapper
public interface AnniversaryMapper {
    /**
     * 根据ID查询纪念日
     * @param id 纪念日ID
     * @return 纪念日对象
     */
    Anniversary findById(Long id);

    /**
     * 根据用户ID查询所有纪念日
     * @param userId 用户ID
     * @return 纪念日列表
     */
    List<Anniversary> findByUserId(Long userId);

    /**
     * 插入新纪念日
     * @param anniversary 纪念日对象
     * @return 影响的行数
     */
    int insert(Anniversary anniversary);

    /**
     * 更新纪念日信息
     * @param anniversary 纪念日对象
     * @return 影响的行数
     */
    int update(Anniversary anniversary);

    /**
     * 删除纪念日
     * @param id 纪念日ID
     * @return 影响的行数
     */
    int delete(Long id);
} 