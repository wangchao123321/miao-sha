package com.wangchao.miaosha.service;

import com.wangchao.miaosha.dao.MiaoShaUserDao;
import com.wangchao.miaosha.domain.MiaoshaUser;
import com.wangchao.miaosha.exception.GlobleException;
import com.wangchao.miaosha.redis.MiaoshaUserKey;
import com.wangchao.miaosha.redis.RedisService;
import com.wangchao.miaosha.result.CodeMsg;
import com.wangchao.miaosha.result.Result;
import com.wangchao.miaosha.util.MD5Util;
import com.wangchao.miaosha.util.UUIDUtil;
import com.wangchao.miaosha.vo.LoginVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Service
public class MiaoShaUserService {

    public static final String COOKIE_NAME_TOKEN = "token";

    @Autowired
    private MiaoShaUserDao userDao;

    @Autowired
    private RedisService redisService;

    public boolean login(HttpServletResponse response,LoginVo loginVo){
        if(loginVo == null){
            throw  new GlobleException(CodeMsg.SERVER_ERROR);
        }
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();

        MiaoshaUser user = userDao.getById(Long.parseLong(mobile));
        if(user == null){
            throw  new GlobleException(CodeMsg.MOBILE_NOT_EXIST);
        }
        String dbPass = user.getPassword();
        String slatDB = user.getSalt();
        if(!dbPass.equals(MD5Util.formPassToDBPass(password,slatDB))){
            throw  new GlobleException(CodeMsg.PASSWORD_ERROR);
        }
        addCookie(user,response);
        return true;
    }

    public MiaoshaUser getByToken(String token,HttpServletResponse response) {
        if(StringUtils.isBlank(token)){
            return null;
        }
        MiaoshaUser user = redisService.get(MiaoshaUserKey.token, token, MiaoshaUser.class);
        // 延长有效期
        if(user != null){
            addCookie(user,response);
        }
        return user;
    }

    private void addCookie(MiaoshaUser user,HttpServletResponse response){
        // 生成cookie
        String token = UUIDUtil.uuid();
        redisService.set(MiaoshaUserKey.token,token,user);
        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN,token);
        cookie.setMaxAge(MiaoshaUserKey.token.expireSeconds());
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
