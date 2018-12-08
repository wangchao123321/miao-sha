package com.wangchao.miaosha.controller;

import com.wangchao.miaosha.rabbitmq.MQSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/hello")
@Controller
public class HelloController {

    @Autowired
    private MQSender mqSender;

    @RequestMapping("/thymeleaf")
    public String thymeleaf(Model model){
        model.addAttribute("name","zhangsan");
        return "hello";
    }

    @RequestMapping("/mq")
    @ResponseBody
    public String mq(){
//        mqSender.send("hello mq");
        return "hello mq";
    }

}
