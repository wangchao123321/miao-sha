package com.wangchao.miaosha.controller;

import com.wangchao.miaosha.domain.MiaoshaOrder;
import com.wangchao.miaosha.domain.MiaoshaUser;
import com.wangchao.miaosha.domain.OrderInfo;
import com.wangchao.miaosha.rabbitmq.MQSender;
import com.wangchao.miaosha.rabbitmq.MiaoshaMessage;
import com.wangchao.miaosha.redis.GoodsKey;
import com.wangchao.miaosha.redis.RedisService;
import com.wangchao.miaosha.result.CodeMsg;
import com.wangchao.miaosha.result.Result;
import com.wangchao.miaosha.service.GoodsService;
import com.wangchao.miaosha.service.MiaoshaService;
import com.wangchao.miaosha.service.OrderService;
import com.wangchao.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/miaosha")
public class MiaoshaController implements InitializingBean{

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private MiaoshaService miaoshaService;

    @Autowired
    private MQSender mqSender;

    @PostMapping("/do_miaosha")
    @ResponseBody
    public Result<Integer> miaosha(Model model, MiaoshaUser user,
                         @RequestParam("goodsId") long goodsId){
        if(user == null){
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        model.addAttribute("user" , user);
        // 预减库存
        long stock = redisService.decr(GoodsKey.getMiaoshaGoodsStock, "" + goodsId);
        if(stock < 0){
            return Result.error(CodeMsg.MIAO_SHA_OVER);
        }
        // 判断是否秒杀到
        MiaoshaOrder miaoshaOrder = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(),goodsId);
        if(miaoshaOrder != null){
            model.addAttribute("errorMsg", CodeMsg.REPEATE_MIAOSHA);
        }
        // 入队
        MiaoshaMessage message = new MiaoshaMessage();
        message.setUser(user);
        message.setGoodsId(goodsId);
        mqSender.sendMiaoshaMessage(message);
        return Result.success(0);

//        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
//        int stock = goods.getStockCount();
//        if(stock <= 0 ){
//            model.addAttribute("errorMsg", CodeMsg.MIAO_SHA_OVER);
//        }
//        MiaoshaOrder miaoshaOrder = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(),goodsId);
//        if(miaoshaOrder != null){
//            model.addAttribute("errorMsg", CodeMsg.REPEATE_MIAOSHA);
//        }
//        // 减库存 下订单 写入秒杀订单
//        OrderInfo orderInfo = miaoshaService.miaosha(user,goods);
//        return Result.success(orderInfo);
    }


    /**
     * orderId: 成功
     * -1: 秒杀失败
     * 0: 排队中
     * @param model
     * @param user
     * @param goodsId
     * @return
     */
    @PostMapping("/result")
    @ResponseBody
    public Result<Long> miaoshaResult(Model model, MiaoshaUser user,
                                   @RequestParam("goodsId") long goodsId) {
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        model.addAttribute("user", user);
        long result = miaoshaService.getMiaoshaResult(user.getId(),goodsId);
        return Result.success(result);
    }



    /**
     * 系统初始化
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> goodsVos = goodsService.listGoodsVo();
        if(goodsVos == null){
            return;
        }
        for (GoodsVo goodsVo : goodsVos) {
            redisService.set(GoodsKey.getMiaoshaGoodsStock  ,""+goodsVo.getId(),goodsVo.getGoodsStock());
        }

    }
}
