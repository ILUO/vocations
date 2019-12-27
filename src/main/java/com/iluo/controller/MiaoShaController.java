package com.iluo.controller;

import com.alibaba.fastjson.JSONObject;
import com.iluo.access.AccessLimit;
import com.iluo.dao.GoodsDAO;
import com.iluo.po.Goods;
import com.iluo.po.GoodsExample;
import com.iluo.po.MiaoshaUser;
import com.iluo.redis.GoodsKey;
import com.iluo.redis.RedisService;
import com.iluo.service.MiaoshaService;
import com.iluo.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Yang Xing Luo on 2019/12/27.
 */

@RestController
@RequestMapping("/miaosha")
@Api(value = "秒杀")
public class MiaoShaController implements InitializingBean{
    @Autowired
    private GoodsDAO goodsDAO;

    @Autowired
    private RedisService redisService;

    @Autowired
    private MiaoshaService miaoshaService;

    private HashMap<Long,Boolean> localHashMap = new HashMap<>();

    @AccessLimit(seconds = 5,maxCount = 5)
    @GetMapping("/{path}/Pessimism")
    @ApiOperation(value = "悲观锁")
    public JSONObject miaoshaPessimism(MiaoshaUser miaoshaUser, @PathVariable("path") String path,
                                       @ApiParam(value = "商品ID") @RequestParam("goodsId") Long goodsId){
        if(!miaoshaService.checkPath(miaoshaUser,goodsId,path)) return CommonUtil.errorJson("秒杀接口错误");
        return miaoshaService.miaoshaPessimism(miaoshaUser,goodsId);
    }

    @GetMapping("/getPath")
    @ApiOperation(value = "获得秒杀接口路径")
    public String getmiaoshaPath(MiaoshaUser miaoshaUser,@ApiParam(value = "商品ID") @RequestParam("goodsId") Long goodsId){
        return miaoshaService.getMiaoshaPath(miaoshaUser,goodsId);
    }



    @Override
    public void afterPropertiesSet(){
        GoodsExample goodsExample = new GoodsExample();
        List<Goods> goodsList = goodsDAO.selectByExample(goodsExample);
        if(goodsList == null) return;
        for(Goods goods:goodsList){
            redisService.set(GoodsKey.getMiaoshaGoodsStock,""+goods.getId(),goods.getGoodsStock());
            localHashMap.put(goods.getId(),false);
        }
    }
}
