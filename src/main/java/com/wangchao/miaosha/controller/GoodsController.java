package com.wangchao.miaosha.controller;

import com.wangchao.miaosha.domain.Goods;
import com.wangchao.miaosha.domain.MiaoshaUser;
import com.wangchao.miaosha.redis.RedisService;
import com.wangchao.miaosha.service.GoodsService;
import com.wangchao.miaosha.service.MiaoShaUserService;
import com.wangchao.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private MiaoShaUserService userService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private RedisService redisService;

    @RequestMapping("/to_list")
    public String toList(Model model,MiaoshaUser user){
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        model.addAttribute("goodsList" , goodsList);
        model.addAttribute("user" , user);
        return "goods_list";
    }

    @RequestMapping("/to_detail/{goodsId}")
    public String detail(Model model, MiaoshaUser user, @PathVariable("goodsId") long goodsId){
        // snowfalke
        model.addAttribute("user" , user);
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        model.addAttribute("goods" , goods);
        long startAt = goods.getStartDate().getTime();
        long endAt = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();

        int miaoshaStatus = 0;
        int remainSeconds = 0;

        if(now < startAt){
            // 秒杀没开始
            miaoshaStatus = 0;
            remainSeconds = (int)((startAt - now)/1000);
        }else if(now > endAt){
            // 秒杀已经结束
            miaoshaStatus = 2;
            remainSeconds = -1;
        }else {
            //秒杀进行中
            miaoshaStatus = 1;
            remainSeconds = 0;
        }
        model.addAttribute("miaoshaStatus" , miaoshaStatus);
        model.addAttribute("remainSeconds" , remainSeconds);
        return "goods_detail";
    }
}
