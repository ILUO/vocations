package com.iluo.dao;

import com.iluo.po.Goods;
import com.iluo.po.GoodsExample;
import org.springframework.stereotype.Repository;

/**
 * GoodsDAO继承基类
 */
@Repository
public interface GoodsDAO extends MyBatisBaseDao<Goods, Long, GoodsExample> {
}