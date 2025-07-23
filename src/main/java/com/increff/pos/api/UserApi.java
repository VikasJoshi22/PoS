package com.increff.pos.api;

import com.increff.pos.dao.UserDao;
import com.increff.pos.pojo.UserPojo;
import com.increff.pos.utils.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = ApiException.class)
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
