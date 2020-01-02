package com.iluo.service;

import com.alibaba.fastjson.JSONObject;
import com.iluo.po.Goods;
import com.iluo.po.MiaoshaOrder;
import com.iluo.po.MiaoshaUser;
import com.iluo.po.OrderInfo;
import com.iluo.vo.GoodsVo;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Yang Xing Luo on 2019/12/27.
 */
public interface OrderService {
    OrderInfo createOrder(MiaoshaUser user, Long goodsId);

    MiaoshaOrder getMiaoshaOrderByUserIdGoodsId(long userId, long goodsId);
}
