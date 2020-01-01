package com.iluo.dao;

import com.iluo.po.MiaoshaOrder;
import com.iluo.po.MiaoshaOrderExample;
import org.springframework.stereotype.Repository;

/**
 * MiaoshaOrderDAO继承基类
 */
@Repository
public interface MiaoshaOrderDAO extends MyBatisBaseDao<MiaoshaOrder, Long, MiaoshaOrderExample> {
}