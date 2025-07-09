package com.increff.pos.api;

import com.increff.pos.dao.OrderDao;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
