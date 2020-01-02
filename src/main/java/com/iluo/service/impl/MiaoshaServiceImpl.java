package com.iluo.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.iluo.config.exception.CommonJsonException;
import com.iluo.dao.MiaoshaGoodsDAO;
import com.iluo.dao.MiaoshaGoodsManualDAO;
import com.iluo.dao.MiaoshaUserDAO;
import com.iluo.po.*;
import com.iluo.rabbitmq.MQSender;
import com.iluo.rabbitmq.MiaoshaMessage;
import com.iluo.redis.GoodsKey;
import com.iluo.redis.MiaoShaUserKey;
import com.iluo.redis.MiaoshaKey;
import com.iluo.redis.RedisService;
import com.iluo.service.GoodsService;
import com.iluo.service.MiaoshaService;
import com.iluo.service.OrderService;
import com.iluo.service.UserService;
import com.iluo.util.CommonUtil;
import com.iluo.util.MD5Utils;
import com.iluo.util.UUIDUtil;
import com.iluo.util.constants.ErrorEnum;
import com.iluo.vo.GoodsVo;
import com.iluo.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;

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

    @Autowired
    private MQSender mqSender;

    @Autowired
    private MiaoshaUserDAO miaoshaUserDAO;


    @Autowired
    private UserService userService;



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

    public String createToken(HttpServletResponse response , LoginVo loginVo) {
        if(loginVo ==null){
            throw  new CommonJsonException(ErrorEnum.E_505);
        }

        String mobile =loginVo.getId().toString();
        String password =loginVo.getPassword();
        MiaoshaUser user = getByNickName(mobile);
        if(user == null) {
            throw new CommonJsonException(ErrorEnum.E_505);
        }

        String dbPass = user.getPassword();
        String saltDb = user.getSalt();
        String calcPass = MD5Utils.formPassToDBPass(password,saltDb);
        if(!calcPass.equals(dbPass)){
            throw new CommonJsonException(ErrorEnum.E_505);
        }
        //生成cookie 将session返回游览器 分布式session
        String token= UUIDUtil.uuid();
        userService.addCookie(response, token, user);
        return token ;
    }

    public MiaoshaUser getByNickName(String nickName) {
        //取缓存
        MiaoshaUser user = redisService.get(MiaoShaUserKey.getByNickName, ""+nickName, MiaoshaUser.class);
        if(user != null) {
            return user;
        }
        //取数据库
        MiaoshaUserExample miaoshaUserExample = new MiaoshaUserExample();
        miaoshaUserExample.createCriteria().andNicknameEqualTo(nickName);
        List<MiaoshaUser> miaoshaUsers= miaoshaUserDAO.selectByExample(miaoshaUserExample);
        if(miaoshaUsers != null) {
            user = miaoshaUsers.get(0);
            redisService.set(MiaoShaUserKey.getByNickName, ""+nickName, user);
        }
        return user;
    }



    @Transactional
    public OrderInfo miaosha(MiaoshaUser user, GoodsVo goods) {
        //减库存 下订单 写入秒杀订单
        int success = miaoshaGoodsManualDAO.reduceStockPessimism(goods.getId());
        if(success > 0){
            return orderService.createOrder(user,goods.getId()) ;
        }else {
            //如果库存不存在则内存标记为true
            setGoodsOver(goods.getId());
            return null;
        }
    }

    private void setGoodsOver(Long goodsId) {
        redisService.set(MiaoshaKey.isGoodsOver, ""+goodsId, true);
    }


    @Override
    public JSONObject miaoshaMessageQueue(MiaoshaUser user,Long goodsId,String path,HashMap<Long,Boolean> hashMap){
        if(user == null) return CommonUtil.errorJson("session不存在或者已经过期");

        //if(!checkPath(user,goodsId,path)) return CommonUtil.errorJson("接口错误");

        MiaoshaOrder miaoshaOrder = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(),goodsId);
        if(miaoshaOrder != null) return CommonUtil.errorJson("已经秒杀到商品");

        if(hashMap.get(goodsId)) return CommonUtil.errorJson("商品秒杀完毕");

        Long stock = redisService.decr(GoodsKey.getMiaoshaGoodsStock,"" + goodsId);
        if(stock < 0){
            hashMap.put(goodsId,true);
            return CommonUtil.errorJson("商品秒杀完毕");
        }

        MiaoshaMessage mm = new MiaoshaMessage();
        mm.setGoodsId(goodsId);
        mm.setUser(user);
        mqSender.sendMiaoshaMessage(mm);
        return CommonUtil.successJson();
    }

}
