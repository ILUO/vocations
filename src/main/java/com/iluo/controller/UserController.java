package com.iluo.controller;

import com.alibaba.fastjson.JSONObject;
import com.iluo.po.MiaoshaUser;
import com.iluo.service.UserService;
import com.iluo.vo.LoginVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by Yang Xing Luo on 2019/12/18.
 */
@RestController
@RequestMapping("/user")
@Api(value = "用户")
public class UserController {
    @Autowired
    private UserService service;

    @ApiOperation(value = "注册")
    @PostMapping(value = "/register")
    public JSONObject Register(HttpServletResponse response,
                               @ApiParam(value = "用户信息") @RequestBody LoginVo loginVo){
        return service.register(response,loginVo);
    }

}
