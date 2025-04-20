package com.couple.space.service.impl;

import com.couple.space.entity.Anniversary;
import com.couple.space.mapper.AnniversaryMapper;
import com.couple.space.service.AnniversaryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * 纪念日服务实现类
 */
@Slf4j
@Service
public class AnniversaryServiceImpl implements AnniversaryService {
    private final AnniversaryMapper anniversaryMapper;

    public AnniversaryServiceImpl(AnniversaryMapper anniversaryMapper) {
        this.anniversaryMapper = anniversaryMapper;
    }

    @Override
    @Transactional
    public Anniversary createAnniversary(Anniversary anniversary) {
        log.info("创建纪念日: {}", anniversary.getName());
        
        // 设置创建时间和更新时间
        anniversary.setCreatedAt(LocalDate.now());
        anniversary.setUpdatedAt(LocalDate.now());
        
        // 插入数据库
        anniversaryMapper.insert(anniversary);
        
        log.info("纪念日创建成功，ID: {}", anniversary.getId());
        return anniversary;
    }

    @Override
    @Transactional
    public Anniversary updateAnniversary(Anniversary anniversary) {
        log.info("更新纪念日: {}", anniversary.getId());
        
        // 设置更新时间
        anniversary.setUpdatedAt(LocalDate.now());
        
        // 更新数据库
        anniversaryMapper.update(anniversary);
        
        log.info("纪念日更新成功");
        return anniversary;
    }

    @Override
    @Transactional
    public void deleteAnniversary(Long id) {
        log.info("删除纪念日: {}", id);
        
        // 删除数据库记录
        anniversaryMapper.delete(id);
        
        log.info("纪念日删除成功");
    }

    @Override
    public Anniversary getAnniversaryById(Long id) {
        log.info("查询纪念日: {}", id);
        
        Anniversary anniversary = anniversaryMapper.findById(id);
        
        if (anniversary == null) {
            log.warn("纪念日不存在: {}", id);
        } else {
            log.info("纪念日查询成功");
        }
        
        return anniversary;
    }

    @Override
    public List<Anniversary> getAnniversariesByUserId(Long userId) {
        log.info("查询用户的所有纪念日: {}", userId);
        
        List<Anniversary> anniversaries = anniversaryMapper.findByUserId(userId);
        
        log.info("查询到 {} 个纪念日", anniversaries.size());
        return anniversaries;
    }
} 