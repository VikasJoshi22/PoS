package com.increff.pos.model.data;

import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
public class OrderData {
    private Integer orderId;
    private Integer productId;
    private Integer quantity;
    private Double sellingPrice;
    private ZonedDateTime dateTime;
    private Boolean orderPlaced;
}
