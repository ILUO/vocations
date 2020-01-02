package com.iluo.service;

import com.iluo.po.MiaoShaMessageInfo;
import com.iluo.vo.MiaoShaMessageVo;

import java.util.List;

/**
 * Created by Yang Xing Luo on 2020/1/2.
 */
public interface MiaoShaMessageService {
    List<MiaoShaMessageInfo> getmessageUserList(Long userId , Integer status );

    void insertMs(MiaoShaMessageVo miaoShaMessageVo);
}
