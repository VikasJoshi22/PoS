package com.increff.pos.model.data;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Setter
@Getter
public class InvoiceData {
    private Integer orderId;
    private String orderDateTime;
    private String invoiceDateTime;
    private Double total;
    private List<InvoiceItemData> invoiceItems;
}
