package com.iluo.vo;

import lombok.Data;

/**
 * Created by Yang Xing Luo on 2019/12/18.
 */
@Data
public class LoginVo {
    /**
     * 用户ID，手机号码
     */
    private Long id;


    private String nickname;

    /**
     * MD5(MD5(pass明文+固定salt) + salt)
     */
    private String password;


    private String salt;
}
