package com.increff.pos.model.data;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class InventoryData {
    private Integer id;
    private Integer productId;
    private Integer quantity;
}
