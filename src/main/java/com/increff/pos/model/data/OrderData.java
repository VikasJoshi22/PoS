package com.increff.pos.model.data;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class OrderData {
    private Integer id;
    private String dateTime;
    private String status;
    private List<OrderItemData> orderItems = new ArrayList<>();

    public void addOrderItem(OrderItemData orderItemData){
        this.orderItems.add(orderItemData);
    }
}
