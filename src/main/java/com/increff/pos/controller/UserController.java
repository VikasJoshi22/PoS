package com.increff.pos.controller;

import com.increff.pos.dao.UserDao;
import com.increff.pos.dto.UserDto;
import com.increff.pos.model.form.UserForm;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api
@RestController
public class UserController {
    @Autowired
    private UserDto userDto;

    @RequestMapping(path = "/register-user", method = RequestMethod.POST)
    public void registerUser(@RequestBody UserForm userForm){
        userDto.registerUser(userForm);
    }
}
