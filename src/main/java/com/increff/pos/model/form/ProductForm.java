package com.increff.pos.model.form;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductForm {
    @NotNull
    private String barcode;
    @NotNull
    private String clientName;
    @NotNull
    private String name;
    @NotNull
    private Double mrp;
    @NotNull
    private String imageUrl;
}
