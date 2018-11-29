package com.wangchao.miaosha.controller;

import com.wangchao.miaosha.domain.MiaoshaUser;
import com.wangchao.miaosha.redis.RedisService;
import com.wangchao.miaosha.service.MiaoShaUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private MiaoShaUserService userService;

    @Autowired
    private RedisService redisService;

    @RequestMapping("/to_list")
    public String toList(Model model,MiaoshaUser user){
        model.addAttribute("user" , user);
        return "goods_list";
    }
}
