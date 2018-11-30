package com.wangchao.miaosha.service;

import com.wangchao.miaosha.dao.GoodsDao;
import com.wangchao.miaosha.domain.Goods;
import com.wangchao.miaosha.domain.MiaoshaUser;
import com.wangchao.miaosha.domain.OrderInfo;
import com.wangchao.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MiaoshaService {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderService orderService;
    @Transactional
    public OrderInfo miaosha(MiaoshaUser user, GoodsVo goods) {

        goodsService.reduceStock(goods);
        // order_info miaosha_info
        return orderService.createOrder(user,goods);
    }
}
