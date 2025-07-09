package com.increff.pos.model.form;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryForm {
    @NotNull
    private Integer productId;
    @NotNull
    private Integer quantity;
}
