package com.iluo.vacations.service;


import com.alibaba.fastjson.JSONObject;
import com.iluo.vacations.dao.MiaoshaUserDAO;
import com.iluo.vacations.po.MiaoshaUser;
import com.iluo.vacations.util.MD5Utils;
import com.iluo.vacations.util.UUIDUtil;
import com.sun.deploy.net.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;

public class UserService {
    @Autowired
    private MiaoshaUserDAO miaoshaUserDAO;

    public JSONObject register(HttpResponse response,MiaoshaUser miaoshaUser, String salt){
        String pwd = MD5Utils.formPassToDBPass(miaoshaUser.getPassword(),salt);
        miaoshaUser.setPassword(pwd);
        try{
            miaoshaUserDAO.insert(miaoshaUser);
            String token= UUIDUtil.uuid();
            addCookie(response, token, user);
        }catch ()
    }
}
