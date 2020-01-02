package com.iluo.controller;

import com.alibaba.fastjson.JSONObject;
import com.iluo.access.AccessLimit;
import com.iluo.service.MiaoshaService;
import com.iluo.service.UserService;
import com.iluo.util.CommonUtil;
import com.iluo.vo.LoginVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * Created by Yang Xing Luo on 2019/12/18.
 */
@RestController
@RequestMapping("/user")
@Api(value = "用户")
public class UserController {
    private static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService service;

    @Autowired
    private MiaoshaService miaoshaService;

    @ApiOperation(value = "注册")
    @PostMapping(value = "/register")
    public JSONObject Register(HttpServletResponse response,
                               @ApiParam(value = "用户信息") @RequestBody LoginVo loginVo){
        return service.register(response,loginVo);
    }

    @ApiOperation(value = "登录")
    @PostMapping(value = "/login")
    public JSONObject Login(HttpServletResponse response,
                            @ApiParam(value = "登录信息") @RequestBody LoginVo loginVo){
        return  service.login(response,loginVo);
    }

    @AccessLimit(seconds = 5, maxCount = 5, needLogin = true)
    @ApiOperation(value = "测试")
    @GetMapping(value = "/test")
    public JSONObject test(){
        return CommonUtil.successJson();
    }


    @PostMapping("/create_token")
    @ResponseBody
    public String createToken(HttpServletResponse response, @Valid LoginVo loginVo) {
        logger.info(loginVo.toString());
        String token = miaoshaService.createToken(response, loginVo);
        return token;
    }

}
