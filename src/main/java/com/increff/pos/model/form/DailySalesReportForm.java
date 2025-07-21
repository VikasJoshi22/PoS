package com.increff.pos.model.form;

import com.increff.pos.utils.Constants;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
public class DailySalesReportForm {
    private Integer page;
    private Integer size;
    private String startDate = Constants.MIN_DATE;
    private String endDate = ZonedDateTime.now().toString();
}
