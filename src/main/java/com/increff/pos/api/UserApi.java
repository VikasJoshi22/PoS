package com.increff.pos.api;

import com.increff.pos.dao.UserDao;
import com.increff.pos.pojo.UserPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserApi {
    @Autowired
    private UserDao userDao;

    public UserPojo getByEmail(String email) {
        return userDao.getByEmail(email);
    }

    public void add(UserPojo userPojo) {
        userDao.add(userPojo);
    }
}
