package com.increff.pos.model.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderError {
    private Integer index;
    private String barcode;
    private String message;
}
