package com.iluo.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by Yang Xing Luo on 2019/12/26.
 */

@Repository
public interface MiaoshaGoodsManualDAO {
    int reduceStockPessimism(@Param("id") long id);
}
