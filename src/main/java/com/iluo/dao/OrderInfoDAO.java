package com.iluo.dao;

import com.iluo.po.OrderInfo;
import com.iluo.po.OrderInfoExample;
import org.springframework.stereotype.Repository;

/**
 * OrderInfoDAO继承基类
 */
@Repository
public interface OrderInfoDAO extends MyBatisBaseDao<OrderInfo, Long, OrderInfoExample> {
}