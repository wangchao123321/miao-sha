package com.wangchao.miaosha.service;

import com.wangchao.miaosha.dao.UserDao;
import com.wangchao.miaosha.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    public User getById(int id){
        User user = userDao.getById(id);
        return user;
    }

    @Transactional
    public void save(){
        User user1 = new User();
        user1.setId(3);
        user1.setName("张三");
        userDao.insert(user1);

        User user2 = new User();
        user2.setId(4);
        user2.setName("张三222");
        userDao.insert(user2);
    }
}
