package com.wangchao.miaosha.controller;

import com.wangchao.miaosha.domain.MiaoshaUser;
import com.wangchao.miaosha.domain.User;
import com.wangchao.miaosha.redis.RedisService;
import com.wangchao.miaosha.redis.UserKey;
import com.wangchao.miaosha.result.Result;
import com.wangchao.miaosha.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private RedisService redisService;

    @Autowired
    private UserService userService;

    @RequestMapping("/info")
    @ResponseBody
    public Result<MiaoshaUser> info(Model model, MiaoshaUser user){
        return Result.success(user);
    }


    @RequestMapping("/user")
    @ResponseBody
    public User user(@RequestParam("id") int id){
        return userService.getById(id);
    }

    @RequestMapping("/save")
    @ResponseBody
    public void save(){
        userService.save();
    }

    @RequestMapping("/redis/set")
    @ResponseBody
    public void redisSet(){
        boolean flag = redisService.set(UserKey.getById,""+1,new User(1,"zhangsan"));
        User user = redisService.get(UserKey.getById,""+1,User.class);
        System.out.println(user);
    }

    @RequestMapping("/redis/get")
    @ResponseBody
    public void redisGet(){
        User user = redisService.get(UserKey.getById,""+1,User.class);
        System.out.println(user);
    }


}
