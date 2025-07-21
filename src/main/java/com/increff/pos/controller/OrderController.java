package com.increff.pos.controller;

import com.increff.pos.dto.InvoiceDto;
import com.increff.pos.dto.OrderDto;
import com.increff.pos.model.data.ErrorData;
import com.increff.pos.model.data.OrderData;
import com.increff.pos.model.data.OrderError;
import com.increff.pos.model.form.OrderFilters;
import com.increff.pos.model.form.OrderForm;
import com.increff.pos.pojo.OrderPojo;
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
    @Autowired
    private InvoiceDto invoiceDto;


    @ApiOperation("create bulk order")
    @RequestMapping(path = "/supervisor/create", method = RequestMethod.PUT)
    public ErrorData<OrderError> create(@RequestBody List<OrderForm> orderFormList) throws ApiException{
        return orderDto.create(orderFormList);
    }

    @ApiOperation("getting an order details")
    @RequestMapping(path = "/get/{orderId}", method = RequestMethod.GET)
    public OrderData getOrderDetails(@PathVariable Integer orderId) throws ApiException{
        return orderDto.getOrderDetails(orderId);
    }

    @RequestMapping(path = "/invoiced/{id}", method = RequestMethod.PUT)
    public void makeOrderInvoiced(@PathVariable Integer id, OrderPojo orderPojo) throws ApiException{
        orderDto.makeOrderInvoiced(id);
    }

    @ApiOperation("getting all order's detail.")
    @RequestMapping(path = "/get-all", method = RequestMethod.GET)
    public List<OrderData> getAll(@ModelAttribute OrderFilters orderfilters) throws ApiException{
        return orderDto.getAll(orderfilters);
    }

    @ApiOperation("getting total no. of orders")
    @RequestMapping(path = "/get-total-count", method = RequestMethod.GET)
    public Long getTotalCount(@ModelAttribute OrderFilters orderFilters){
        return orderDto.getTotalCount(orderFilters);
    }

}
