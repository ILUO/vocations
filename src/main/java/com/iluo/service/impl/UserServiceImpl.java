package com.iluo.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.iluo.dao.MiaoshaUserDAO;
import com.iluo.po.MiaoshaUser;
import com.iluo.po.MiaoshaUserExample;
import com.iluo.redis.MiaoShaUserKey;
import com.iluo.redis.RedisService;
import com.iluo.service.UserService;
import com.iluo.util.CommonUtil;
import com.iluo.util.MD5Utils;
import com.iluo.util.UUIDUtil;
import com.iluo.vo.LoginVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private MiaoshaUserDAO miaoshaUserDAO;

    @Autowired
    private RedisService redisService;

    @Override
    public JSONObject register(HttpServletResponse response, LoginVo loginVo){
        MiaoshaUser miaoshaUser = new MiaoshaUser();
        miaoshaUser.setId(loginVo.getId());
        miaoshaUser.setNickname(loginVo.getNickname());
        miaoshaUser.setPassword(loginVo.getPassword());
        miaoshaUser.setSalt(loginVo.getSalt());
        String pwd = MD5Utils.formPassToDBPass(miaoshaUser.getPassword(),miaoshaUser.getSalt());
        miaoshaUser.setPassword(pwd);
        try{
            miaoshaUserDAO.insert(miaoshaUser);
            String token= UUIDUtil.uuid();
            addCookie(response, token, miaoshaUser);
        }catch (Exception e){
            e.printStackTrace();
            return CommonUtil.errorJson("注册失败");
        }
        return CommonUtil.successJson();
    }


    @Override
    public JSONObject login(HttpServletResponse response, LoginVo loginVo){
        Long id = loginVo.getId();
        MiaoshaUserExample miaoshaUserExample = new MiaoshaUserExample();
        miaoshaUserExample.createCriteria().andIdEqualTo(id);
        List<MiaoshaUser> miaoshaUsers = miaoshaUserDAO.selectByExample(miaoshaUserExample);
        if(miaoshaUsers.size() == 0) return CommonUtil.errorJson("登录名或者密码错误");

        MiaoshaUser miaoshaUser = miaoshaUsers.get(0);
        if(MD5Utils.formPassToDBPass(loginVo.getPassword(),miaoshaUser.getSalt()).equals(miaoshaUser.getPassword())){
            String token = UUIDUtil.uuid();
            addCookie(response, token,miaoshaUser);
            return CommonUtil.successJson();
        }else{
            return CommonUtil.errorJson("登录名或者密码错误");
        }
    }


    @Override
    public void addCookie(HttpServletResponse response, String token, MiaoshaUser user) {
        redisService.set(MiaoShaUserKey.token, token, user);
        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);
        //设置有效期
        cookie.setMaxAge(MiaoShaUserKey.token.expireSeconds());
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    @Override
    public MiaoshaUser getByToken(HttpServletResponse response , String token) {

        if(StringUtils.isEmpty(token)){
            return null ;
        }
        MiaoshaUser user = redisService.get(MiaoShaUserKey.token,token,MiaoshaUser.class) ;
        if(user!=null) {
            addCookie(response, token, user);
        }
        return user ;
    }
}
