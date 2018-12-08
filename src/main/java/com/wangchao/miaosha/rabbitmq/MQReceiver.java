package com.wangchao.miaosha.rabbitmq;

import com.wangchao.miaosha.domain.MiaoshaOrder;
import com.wangchao.miaosha.domain.MiaoshaUser;
import com.wangchao.miaosha.domain.OrderInfo;
import com.wangchao.miaosha.redis.RedisService;
import com.wangchao.miaosha.service.GoodsService;
import com.wangchao.miaosha.service.MiaoshaService;
import com.wangchao.miaosha.service.OrderService;
import com.wangchao.miaosha.vo.GoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MQReceiver {

    private static Logger log = LoggerFactory.getLogger(MQReceiver.class);

    @Autowired
    private GoodsService goodsService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private MiaoshaService miaoshaService;

    @RabbitListener(queues = MQConfig.MIAOSHA_QUEUE)
    public void recerve(String message){
        log.info("receive message " + message);
        MiaoshaMessage mm = RedisService.stringToBean(message, MiaoshaMessage.class);
        long goodsId = mm.getGoodsId();
        MiaoshaUser user = mm.getUser();
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        int stock = goods.getStockCount();
        if(stock <= 0 ){
            return;
        }
        MiaoshaOrder miaoshaOrder = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(),goodsId);
        if(miaoshaOrder != null){
            return;
        }
        OrderInfo orderInfo = miaoshaService.miaosha(user,goods);
    }


//    @RabbitListener(queues = MQConfig.QUEUE)
//    public void receive(String message) {
//        log.info("receive message " + message);
//    }
//
//    @RabbitListener(queues = MQConfig.TOPIC_QUEUE1)
//    public void receiveTopic1(String message) {
//        log.info(" topic  queue1 message:" + message);
//    }
//
//    @RabbitListener(queues = MQConfig.TOPIC_QUEUE2)
//    public void receiveTopic2(String message) {
//        log.info(" topic  queue2 message:" + message);
//    }
//
//    @RabbitListener(queues = MQConfig.HEADER_QUEUE)
//    public void receiveHeaderQueue(byte[] message) {
//        log.info(" header  queue message:" + new String(message));
//    }
}
