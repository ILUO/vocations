package com.iluo.vacations.dao;

import com.iluo.vacations.po.MiaoshaUser;
import com.iluo.vacations.po.MiaoshaUserExample;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * MiaoshaUserDAO继承基类
 */
@Mapper
@Repository
public interface MiaoshaUserDAO extends MyBatisBaseDao<MiaoshaUser, Long, MiaoshaUserExample> {
}