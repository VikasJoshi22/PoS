package com.increff.pos.controller;

import com.increff.pos.dto.UserDto;
import com.increff.pos.model.form.UserForm;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@Api
@RestController
public class UserController {
    @Autowired
    private UserDto userDto;

    @RequestMapping(path = "/register-user", method = RequestMethod.POST)
    public void registerUser(@RequestBody UserForm userForm){
        userDto.registerUser(userForm);
    }

    @RequestMapping(path = "/api/user-info", method = RequestMethod.GET)
    public ResponseEntity<?> getUserInfo(Authentication authentication) {
        if (!Objects.isNull(authentication) && !Objects.isNull(authentication.getPrincipal())) {
            return ResponseEntity.ok(authentication.getPrincipal()); // or custom user object
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
