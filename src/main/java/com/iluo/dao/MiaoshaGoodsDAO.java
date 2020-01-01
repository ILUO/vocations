package com.iluo.dao;

import com.iluo.po.MiaoshaGoods;
import com.iluo.po.MiaoshaGoodsExample;
import org.springframework.stereotype.Repository;

/**
 * MiaoshaGoodsDAO继承基类
 */
@Repository
public interface MiaoshaGoodsDAO extends MyBatisBaseDao<MiaoshaGoods, Long, MiaoshaGoodsExample> {
}