package com.wangchao.miaosha.service;

import com.wangchao.miaosha.dao.GoodsDao;
import com.wangchao.miaosha.domain.Goods;
import com.wangchao.miaosha.domain.MiaoshaOrder;
import com.wangchao.miaosha.domain.MiaoshaUser;
import com.wangchao.miaosha.domain.OrderInfo;
import com.wangchao.miaosha.redis.MiaoshaKey;
import com.wangchao.miaosha.redis.MiaoshaUserKey;
import com.wangchao.miaosha.redis.RedisService;
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
    @Autowired
    private RedisService redisService;

    @Transactional
    public OrderInfo miaosha(MiaoshaUser user, GoodsVo goods) {

        boolean b = goodsService.reduceStock(goods);
        if (b) {
            return orderService.createOrder(user, goods);
        } else {
            setGoodsOver(goods.getId());
            return null;
        }
    }

    public long getMiaoshaResult(Long id, long goodsId) {
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(id, goodsId);
        if(order != null){
            return order.getOrderId();
        }else {
            boolean isOver = getGoodsOver(goodsId);
            if(isOver){
                return -1;
            }else{
                return 0;
            }
        }
    }

    private boolean getGoodsOver(long goodsId) {
        return redisService.exists(MiaoshaKey.isGoodsOver,""+goodsId);
    }

    public void setGoodsOver(long goodsId) {
        redisService.set(MiaoshaKey.isGoodsOver,""+goodsId,true);
    }
}
