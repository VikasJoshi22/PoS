package com.increff.pos.api;

import com.increff.pos.dao.OrderDao;
import com.increff.pos.model.form.OrderFilters;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.utils.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class OrderApi {
    @Autowired
    private OrderDao orderDao;

    public Integer addOrder(OrderPojo orderPojo){
        return orderDao.addOrder(orderPojo);
    }

    public List<OrderPojo> getAllOrders(OrderFilters orderFilters) throws ApiException{
        Long totalCount = orderDao.getTotalCount(orderFilters);
        if(totalCount!=0 && (long) orderFilters.getPage()*orderFilters.getSize() >= totalCount){
            // not gonna invoked by fronted, most probably.
            throw new ApiException("invalid page number");
        }
        return orderDao.getAllOrders(orderFilters);
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

    public List<OrderPojo> getBetweenDates(ZonedDateTime startDate, ZonedDateTime endDate){
        return orderDao.getBetweenDates(startDate, endDate);
    }

    public Long getTotalCount(OrderFilters orderFilters) {
        return orderDao.getTotalCount(orderFilters);
    }
}
