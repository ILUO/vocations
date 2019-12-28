package com.iluo.service.impl;

import com.iluo.dao.GoodsDAO;
import com.iluo.dao.GoodsManualDAO;
import com.iluo.po.Goods;
import com.iluo.service.GoodsService;
import com.iluo.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Yang Xing Luo on 2019/12/28.
 */
@Service
public class GoodsServiceImpl implements GoodsService {
    @Autowired
    private GoodsManualDAO goodsManualDAO;

    @Override
    public GoodsVo getGoodsVoById(Long goodsId){
        return goodsManualDAO.getGoodsVoByGoodsId(goodsId);
    }
}
