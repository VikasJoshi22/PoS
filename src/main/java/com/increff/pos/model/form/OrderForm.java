package com.increff.pos.model.form;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderForm {
    @NotNull
    private String barcode;
    @NotNull
    private Integer quantity;
    @NotNull
    private Double sellingPrice;
}
