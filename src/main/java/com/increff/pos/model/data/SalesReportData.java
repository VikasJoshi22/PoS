package com.increff.pos.model.data;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SalesReportData {
    private String client;
    private String productBarcode;
    private Integer quantity;
    private Double revenue;
}
