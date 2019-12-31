package com.iluo.service.impl;

import com.iluo.dao.MiaoshaOrderDAO;
import com.iluo.dao.OrderInfoDAO;
import com.iluo.po.MiaoshaOrder;
import com.iluo.po.MiaoshaUser;
import com.iluo.po.OrderInfo;
import com.iluo.redis.OrderKey;
import com.iluo.redis.RedisService;
import com.iluo.service.GoodsService;
import com.iluo.service.OrderService;
import com.iluo.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;


/**
 * Created by Yang Xing Luo on 2019/12/27.
 */
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderInfoDAO orderDao;

    @Autowired
    private MiaoshaOrderDAO miaoshaOrderDAO;

    @Autowired
    private RedisService redisService;

    @Autowired
    private GoodsService goodsService;

    @Override
    @Transactional
    public OrderInfo createOrder(MiaoshaUser user, Long goodsId) {
        GoodsVo goods = goodsService.getGoodsVoById(goodsId);
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setCreateDate(new Date());
        orderInfo.setDeliveryAddrId(0L);
        orderInfo.setGoodsCount(1);
        orderInfo.setGoodsId(goods.getId());
        orderInfo.setGoodsName(goods.getGoodsName());
        orderInfo.setGoodsPrice(new BigDecimal(goods.getMiaoshaPrice().toString()));
        orderInfo.setOrderChannel(1);
        orderInfo.setStatus(0);
        orderInfo.setUserId(user.getId());
        orderDao.insert(orderInfo);
        MiaoshaOrder miaoshaOrder = new MiaoshaOrder();
        miaoshaOrder.setGoodsId(goods.getId());
        miaoshaOrder.setOrderId(orderInfo.getId());
        miaoshaOrder.setUserId(user.getId());
        miaoshaOrderDAO.insert(miaoshaOrder);
        redisService.set(OrderKey.getMiaoshaOrderByUidGid,""+user.getNickname()+"_"+goods.getId(),miaoshaOrder) ;
        return orderInfo;
    }
}
