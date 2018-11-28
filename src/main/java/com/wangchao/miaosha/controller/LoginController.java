package com.wangchao.miaosha.controller;

import com.wangchao.miaosha.result.CodeMsg;
import com.wangchao.miaosha.result.Result;
import com.wangchao.miaosha.service.MiaoShaUserService;
import com.wangchao.miaosha.service.UserService;
import com.wangchao.miaosha.util.ValidatorUtil;
import com.wangchao.miaosha.vo.LoginVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
@RequestMapping("/login")
public class LoginController {

    private static Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private MiaoShaUserService userService;

    @RequestMapping("/to_login")
    public String tologin(){
        return "login";
    }

    @RequestMapping("/do_login")
    @ResponseBody
    public Result<Boolean> doLogin(@Valid LoginVo loginVo, HttpServletResponse response){
        logger.info(loginVo.toString());
        boolean result = userService.login(response,loginVo);
        return Result.success(true);
    }

}

