package com.iluo.service;

import com.alibaba.fastjson.JSONObject;
import com.iluo.po.MiaoshaUser;
import com.iluo.po.OrderInfo;
import com.iluo.vo.GoodsVo;
import com.iluo.vo.LoginVo;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

/**
 * Created by Yang Xing Luo on 2019/12/26.
 */
public interface MiaoshaService {
    JSONObject miaoshaPessimism(MiaoshaUser user,Long id);

    String getMiaoshaPath(MiaoshaUser user,Long goodsId);

    Boolean checkPath(MiaoshaUser user,Long goodsId,String Path);

    JSONObject miaoshaOptimism(MiaoshaUser user,Long goodsId);

    String createToken(HttpServletResponse response , LoginVo loginVo);

    OrderInfo miaosha(MiaoshaUser user, GoodsVo goods);

    JSONObject miaoshaMessageQueue(MiaoshaUser miaoshaUser, Long goodsId, String path, HashMap<Long,Boolean> hashMap);

}
