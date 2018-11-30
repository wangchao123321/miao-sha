package com.wangchao.miaosha.controller;

import com.wangchao.miaosha.domain.MiaoshaOrder;
import com.wangchao.miaosha.domain.MiaoshaUser;
import com.wangchao.miaosha.domain.OrderInfo;
import com.wangchao.miaosha.result.CodeMsg;
import com.wangchao.miaosha.service.GoodsService;
import com.wangchao.miaosha.service.MiaoshaService;
import com.wangchao.miaosha.service.OrderService;
import com.wangchao.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/miaosha")
public class MiaoshaController {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private MiaoshaService miaoshaService;

    @RequestMapping("/do_miaosha")
    public String toList(Model model, MiaoshaUser user,
                         @RequestParam("goodsId") long goodsId){
        if(user == null){
            return "login";
        }
        model.addAttribute("user" , user);
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        int stock = goods.getStockCount();
        if(stock <= 0 ){
            model.addAttribute("errorMsg", CodeMsg.MIAO_SHA_OVER.getMsg());
            return "miaosha_fail";
        }
        MiaoshaOrder miaoshaOrder = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(),goodsId);
        if(miaoshaOrder != null){
            model.addAttribute("errorMsg", CodeMsg.REPEATE_MIAOSHA.getMsg());
            return "miaosha_fail";
        }
        // 减库存 下订单 写入秒杀订单
        OrderInfo orderInfo = miaoshaService.miaosha(user,goods);
        model.addAttribute("orderInfo", orderInfo);
        model.addAttribute("goods", goods);
        return "order_detail";
    }
}
