package com.wangchao.miaosha.service;

import com.wangchao.miaosha.dao.MiaoShaUserDao;
import com.wangchao.miaosha.domain.MiaoshaUser;
import com.wangchao.miaosha.exception.GlobleException;
import com.wangchao.miaosha.result.CodeMsg;
import com.wangchao.miaosha.result.Result;
import com.wangchao.miaosha.util.MD5Util;
import com.wangchao.miaosha.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MiaoShaUserService {

    @Autowired
    private MiaoShaUserDao userDao;

    public boolean login(LoginVo loginVo){
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
        return true;
    }
}
