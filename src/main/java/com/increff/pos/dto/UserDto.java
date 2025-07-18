package com.increff.pos.dto;

import com.increff.pos.api.UserApi;
import com.increff.pos.model.form.UserForm;
import com.increff.pos.pojo.UserPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserDto {
    @Autowired
    private UserApi userApi;

    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    public void registerUser(UserForm userForm){
        UserPojo userPojo = new UserPojo();
        userPojo.setEmail(userForm.getEmail());
        userPojo.setPassword(bCryptPasswordEncoder.encode(userForm.getPassword()));
        userApi.add(userPojo);
    }
}
