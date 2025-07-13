package com.increff.pos.controller;

import com.increff.pos.dto.OrderDto;
import com.increff.pos.model.data.OrderData;
import com.increff.pos.model.data.OrderError;
import com.increff.pos.model.form.OrderForm;
import com.increff.pos.utils.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderDto orderDto;

    @ApiOperation("create bulk order")
    @RequestMapping(path = "/create", method = RequestMethod.PUT)
    public List<OrderError> create(@RequestBody List<OrderForm> orderFormList) throws ApiException{
        return orderDto.create(orderFormList);
    }

    @ApiOperation("getting an order details")
    @RequestMapping(path = "/get/{orderId}", method = RequestMethod.GET)
    public OrderData getOrderDetails(@PathVariable Integer orderId) throws ApiException{
        return orderDto.getOrderDetails(orderId);
    }

    @ApiOperation("Getting all order's detail")
    @RequestMapping(path = "/get-all", method = RequestMethod.GET)
    public List<OrderData> getAllOrdersDetail() throws ApiException{
        return orderDto.getAllOrderDetails();
    }

//    @ApiOperation("getting all order's detail.")
//    @RequestMapping(path = "get-all", method = RequestMethod.GET)
//    public List<OrderData> getAll(){
//        return orderDto.getAll();
//    }

}
