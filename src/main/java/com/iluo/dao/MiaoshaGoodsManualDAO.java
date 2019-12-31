package com.iluo.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * Created by Yang Xing Luo on 2019/12/26.
 */

@Repository
@Mapper
public interface MiaoshaGoodsManualDAO {
    int reduceStockPessimism(@Param("id") long id);

    @Update("update miaosha_goods set stock_count = #{stockCount} - 1,version = #{version} + 1 " +
            "where goods_id = #{goodsId} and version = #{version}")
    int reduceStockOptimism(@Param("goodsId") long goodsId,@Param("version") int version,@Param("stockCount") int stock);
}
