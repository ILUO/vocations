package com.iluo.service;


import com.alibaba.fastjson.JSONObject;
import com.iluo.po.MiaoshaUser;
import com.iluo.vo.LoginVo;


import javax.servlet.http.HttpServletResponse;

public interface UserService {
    String COOKIE_NAME_TOKEN = "token" ;

    JSONObject register(HttpServletResponse response, LoginVo loginVo);

    JSONObject login(HttpServletResponse response, LoginVo loginVo);

    MiaoshaUser getByToken(HttpServletResponse response , String token);

    void addCookie(HttpServletResponse response, String token, MiaoshaUser user);
}
