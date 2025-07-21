package com.increff.pos.model.form;

import com.increff.pos.utils.Constants;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
public class OrderFilters {
    private Integer page = 0;
    private Integer size = 10;
    private String startDate = Constants.MIN_DATE;
    private String endDate = ZonedDateTime.now().toString();
    private Integer orderId;
    private String status = "";
}
