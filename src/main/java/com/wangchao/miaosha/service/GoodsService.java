package com.wangchao.miaosha.service;

import com.wangchao.miaosha.dao.GoodsDao;
import com.wangchao.miaosha.domain.Goods;
import com.wangchao.miaosha.domain.MiaoshaGoods;
import com.wangchao.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsService {

    @Autowired
    private GoodsDao goodsDao;

    public List<GoodsVo> listGoodsVo(){
        return goodsDao.listGoodsVo();
    }

    public GoodsVo getGoodsVoByGoodsId(long goodsId) {
        return goodsDao.getGoodsVoByGoodsId(goodsId);
    }

    public boolean reduceStock(GoodsVo goods) {
        // 减库存 下订单 写入秒杀订单
        MiaoshaGoods g = new MiaoshaGoods();
        g.setGoodsId(goods.getId());
        int i = goodsDao.reduceStock(g);
        return i > 0;
    }
}
