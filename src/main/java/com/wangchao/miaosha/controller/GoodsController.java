package com.wangchao.miaosha.controller;

import com.wangchao.miaosha.domain.MiaoshaUser;
import com.wangchao.miaosha.redis.RedisService;
import com.wangchao.miaosha.service.MiaoShaUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private MiaoShaUserService userService;

    @Autowired
    private RedisService redisService;

    @RequestMapping("/to_list")
    public String toList(Model model,HttpServletResponse response,
                         @CookieValue(value = MiaoShaUserService.COOKIE_NAME_TOKEN,required = false) String cookieToken,
                         @RequestParam(value = MiaoShaUserService.COOKIE_NAME_TOKEN,required = false) String paramToken){
        if(StringUtils.isBlank(cookieToken) && StringUtils.isBlank(paramToken)){
            return "login";
        }
        String token = StringUtils.isEmpty(paramToken) ? cookieToken : paramToken;
        MiaoshaUser user = userService.getByToken(token,response);
        model.addAttribute("user" , user);
        return "goods_list";
    }
}
