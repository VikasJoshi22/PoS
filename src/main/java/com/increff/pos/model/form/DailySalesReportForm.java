package com.increff.pos.model.form;

import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
public class DailySalesReportForm {
    private String startDate = "2025-07-15T10:47:38.803+05:30";
    private String endDate = ZonedDateTime.now().toString();
}
