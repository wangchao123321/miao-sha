package com.wangchao.miaosha.service;

import com.wangchao.miaosha.dao.MiaoShaUserDao;
import com.wangchao.miaosha.domain.MiaoshaUser;
import com.wangchao.miaosha.exception.GlobleException;
import com.wangchao.miaosha.redis.MiaoshaUserKey;
import com.wangchao.miaosha.redis.RedisService;
import com.wangchao.miaosha.result.CodeMsg;
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

    public MiaoshaUser getById(long id){
        MiaoshaUser user = redisService.get(MiaoshaUserKey.getById, "" + id, MiaoshaUser.class);
        if(user != null){
            return user;
        }
        user = userDao.getById(id);
        if(user != null){
            redisService.set(MiaoshaUserKey.getById,"" + id,user);
        }
        return user;
    }

    public boolean updatePassword(String token, long id, String passwordNew){
        MiaoshaUser user = getById(id);
        if(user == null){
            throw new GlobleException(CodeMsg.MOBILE_NOT_EXIST);
        }
        MiaoshaUser toBeUpdate = new MiaoshaUser();
        toBeUpdate.setId(id);
        toBeUpdate.setPassword(MD5Util.formPassToDBPass(passwordNew,user.getSalt()));
        userDao.update(toBeUpdate);
        redisService.delete(MiaoshaUserKey.getById,"" + id);
        user.setPassword(toBeUpdate.getPassword());
        redisService.set(MiaoshaUserKey.token,token,user);
        return true;
    }


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
        // 生成cookie
        String token = UUIDUtil.uuid();
        addCookie(user,token,response);
        return true;
    }

    public MiaoshaUser getByToken(String token,HttpServletResponse response) {
        if(StringUtils.isBlank(token)){
            return null;
        }
        MiaoshaUser user = redisService.get(MiaoshaUserKey.token, token, MiaoshaUser.class);
        // 延长有效期
        if(user != null){
            addCookie(user,token,response);
        }
        return user;
    }

    private void addCookie(MiaoshaUser user,String token,HttpServletResponse response){

        redisService.set(MiaoshaUserKey.token,token,user);
        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN,token);
        cookie.setMaxAge(MiaoshaUserKey.token.expireSeconds());
        cookie.setPath("/");
        cookie.setDomain("");
        response.addCookie(cookie);
    }
}
