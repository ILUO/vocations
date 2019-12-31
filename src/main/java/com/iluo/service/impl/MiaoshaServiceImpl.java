package com.iluo.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.iluo.dao.MiaoshaGoodsDAO;
import com.iluo.dao.MiaoshaGoodsManualDAO;
import com.iluo.po.MiaoshaGoods;
import com.iluo.po.MiaoshaUser;
import com.iluo.redis.MiaoshaKey;
import com.iluo.redis.RedisService;
import com.iluo.service.GoodsService;
import com.iluo.service.MiaoshaService;
import com.iluo.service.OrderService;
import com.iluo.util.CommonUtil;
import com.iluo.util.MD5Utils;
import com.iluo.util.UUIDUtil;
import com.iluo.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Yang Xing Luo on 2019/12/26.
 */
@Service
public class MiaoshaServiceImpl implements MiaoshaService {
    @Autowired
    private MiaoshaGoodsManualDAO miaoshaGoodsManualDAO;

    @Autowired
    private RedisService redisService;

    @Autowired
    private MiaoshaGoodsDAO miaoshaGoodsDAO;

    @Autowired
    private OrderService orderService;

    @Autowired
    private GoodsService goodsService;

    @Override
    public JSONObject miaoshaPessimism(MiaoshaUser miaoshaUser,Long id){
        int res = miaoshaGoodsManualDAO.reduceStockPessimism(id);
        if(res > 0){
            orderService.createOrder(miaoshaUser,id);
            return CommonUtil.successJson();
        }
        else return CommonUtil.errorJson("秒杀失败");
    }


    @Override
    public JSONObject miaoshaOptimism(MiaoshaUser miaoshaUser,Long goodsId){
        int res = 0;
        for(int i = 1;i < 5;i++){
            MiaoshaGoods miaoshaGoods = miaoshaGoodsDAO.selectByPrimaryKey(goodsId);
            int version = miaoshaGoods.getVersion();
            int stock = miaoshaGoods.getStockCount();
            if(stock <= 0) return CommonUtil.errorJson("商品已经抢光");
            res = miaoshaGoodsManualDAO.reduceStockOptimism(goodsId,version,stock);
            if(res > 0) break;
        }
        if(res > 0){
            orderService.createOrder(miaoshaUser,goodsId);
            return CommonUtil.successJson();
        }else return CommonUtil.errorJson("秒杀失败");

    }


    @Override
    public String getMiaoshaPath(MiaoshaUser user,Long goodsId){
        if(user == null || goodsId == null) return null;
        String str = MD5Utils.md5(UUIDUtil.uuid()+"123456");
        redisService.set(MiaoshaKey.getMiaoshaPath, ""+user.getNickname() + "_"+ goodsId, str);
        return str;
    }

    @Override
    public Boolean checkPath(MiaoshaUser user,Long goodsId, String path){
        if(user == null || goodsId == null || path == null) return false;
        String p = redisService.get(MiaoshaKey.getMiaoshaPath,""+user.getNickname() + "_" + goodsId,String.class);
        return p.equals(path);
    }
}
