package com.increff.pos.model.form;

import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
public class OrderFilters {
    private Integer page = 0;
    private Integer size = 10;
    private String startDate = "2025-07-15T10:47:38.803+05:30";
    private String endDate = ZonedDateTime.now().toString();
    private Integer orderId;
    private String status = "";
}
