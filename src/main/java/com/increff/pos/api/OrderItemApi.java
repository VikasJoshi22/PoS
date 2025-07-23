package com.increff.pos.api;

import com.increff.pos.dao.OrderItemDao;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.utils.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = ApiException.class)
public class OrderItemApi {
    @Autowired
    private OrderItemDao orderItemDao;

    public void addOrderItem(OrderItemPojo orderItemPojo){
        orderItemDao.addOrderItem(orderItemPojo);
    }

    public List<OrderItemPojo> getAllOrderItems() {
        return orderItemDao.getAllOrderItems();
    }

    public List<OrderItemPojo> getByOrderId(Integer orderId) {
        return orderItemDao.getByOrderId(orderId);
    }
}
