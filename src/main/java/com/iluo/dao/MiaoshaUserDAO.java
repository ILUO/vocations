package com.iluo.dao;

import com.iluo.po.MiaoshaUser;
import com.iluo.po.MiaoshaUserExample;
import org.springframework.stereotype.Repository;

/**
 * MiaoshaUserDAO继承基类
 */
@Repository
public interface MiaoshaUserDAO extends MyBatisBaseDao<MiaoshaUser, Long, MiaoshaUserExample> {
}