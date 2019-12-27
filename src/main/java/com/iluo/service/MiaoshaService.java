package com.iluo.service;

import com.alibaba.fastjson.JSONObject;
import com.iluo.po.MiaoshaUser;

/**
 * Created by Yang Xing Luo on 2019/12/26.
 */
public interface MiaoshaService {
    JSONObject miaoshaPessimism(MiaoshaUser user,Long id);

    String getMiaoshaPath(MiaoshaUser user,Long goodsId);

    Boolean checkPath(MiaoshaUser user,Long goodsId,String Path);
}
