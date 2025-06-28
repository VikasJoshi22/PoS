package com.increff.pos.controller;
import com.increff.pos.model.data.MessageData;
import com.increff.pos.utils.ApiException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class AppRestControllerAdvice {

    @ExceptionHandler(ApiException.class)
    public MessageData handle(ApiException e) {
        MessageData data = new MessageData();
        data.setMessage(e.getMessage());
        return data;
    }

    @ExceptionHandler(Throwable.class)
    public MessageData handle(Throwable e) {
        MessageData data = new MessageData();
        data.setMessage("some unknown exception occured" + e.getMessage());
        return data;
    }
}
