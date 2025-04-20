package com.couple.space.service;

import com.couple.space.entity.Anniversary;
import java.util.List;

/**
 * 纪念日服务接口
 * 定义纪念日相关的业务逻辑方法
 */
public interface AnniversaryService {
    /**
     * 创建纪念日
     * @param anniversary 纪念日对象
     * @return 创建的纪念日对象
     */
    Anniversary createAnniversary(Anniversary anniversary);

    /**
     * 更新纪念日
     * @param anniversary 纪念日对象
     * @return 更新后的纪念日对象
     */
    Anniversary updateAnniversary(Anniversary anniversary);

    /**
     * 删除纪念日
     * @param id 纪念日ID
     */
    void deleteAnniversary(Long id);

    /**
     * 根据ID查询纪念日
     * @param id 纪念日ID
     * @return 纪念日对象
     */
    Anniversary getAnniversaryById(Long id);

    /**
     * 查询用户的所有纪念日
     * @param userId 用户ID
     * @return 纪念日列表
     */
    List<Anniversary> getAnniversariesByUserId(Long userId);
} 