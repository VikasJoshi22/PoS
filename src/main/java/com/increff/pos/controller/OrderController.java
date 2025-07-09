package com.increff.pos.controller;

import com.increff.pos.dto.OrderDto;
import com.increff.pos.model.data.OrderData;
import com.increff.pos.model.form.OrderForm;
import com.increff.pos.utils.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderDto orderDto;

    @ApiOperation("create a customer order")
    @RequestMapping(path = "/create", method = RequestMethod.PUT)
    public void create(@RequestBody OrderForm orderForm) throws ApiException {
        orderDto.create(orderForm);
    }

    @ApiOperation("create bulk order")
    @RequestMapping(path = "batch-create", method = RequestMethod.PUT)
    public void batchCreate(@RequestBody List<OrderForm> orderFormList) throws ApiException{
        orderDto.batchCreate(orderFormList);
    }

    @ApiOperation("getting all order's detail.")
    @RequestMapping(path = "get-all", method = RequestMethod.GET)
    public List<OrderData> getAll(){
        return orderDto.getAll();
    }

}
