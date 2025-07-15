package com.increff.pos.api;

import com.increff.pos.dao.OrderDao;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.utils.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.Objects;

@Service
public class OrderApi {
    @Autowired
    private OrderDao orderDao;

    public void addOrder(OrderPojo orderPojo){
        orderDao.addOrder(orderPojo);
    }

    public List<OrderPojo> getAllOrders(){
        return orderDao.getAllOrders();
    }

    public void makeOrderInvoiced(Integer id) throws ApiException {
        OrderPojo orderPojo = getById(id);
        orderPojo.setOrderInvoiced();
        orderDao.update(id, orderPojo);
    }

    public OrderPojo getById(Integer orderId) throws ApiException {
        OrderPojo orderPojo = orderDao.getById(orderId);
        if(Objects.isNull(orderPojo)){
            throw new ApiException("order with id '"+orderId+"' doesn't exists");
        }
        return orderPojo;
    }

}
