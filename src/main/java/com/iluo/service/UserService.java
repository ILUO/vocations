package com.iluo.service;


import com.alibaba.fastjson.JSONObject;
import com.iluo.po.MiaoshaUser;
import com.iluo.vo.LoginVo;


import javax.servlet.http.HttpServletResponse;

public interface UserService {
    public JSONObject register(HttpServletResponse response, LoginVo loginVo);
}
