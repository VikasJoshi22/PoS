package com.increff.pos.flow;

import com.increff.pos.model.data.InvoiceData;
import com.increff.pos.model.data.InvoiceItemData;
import com.increff.pos.api.OrderApi;
import com.increff.pos.api.OrderItemApi;
import com.increff.pos.api.ProductApi;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.utils.ApiException;
import org.apache.fop.apps.FopFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

@Component
@Transactional(rollbackFor = ApiException.class)
public class InvoiceFlow {
    @Autowired
    private OrderApi orderApi;
    @Autowired
    private OrderItemApi orderItemApi;
    @Autowired
    private ProductApi productApi;
    private final FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());
    private final RestTemplate restTemplate = new RestTemplate();

    public String generateInvoice(Integer orderId) throws ApiException {
        OrderPojo orderPojo = orderApi.getById(orderId);
        if (orderPojo == null) {
            throw new ApiException("Order doesn't exist.\n");
        }
        if(orderPojo.getStatus().equals("invoiced")){
            throw new ApiException("invoice is already generated");
        }

        //changing order status
        orderApi.makeOrderInvoiced(orderId);

        Double total = 0.0;
        List<OrderItemPojo> orderItemPojoList = orderItemApi.getByOrderId(orderId);
        List<InvoiceItemData> invoiceItemDataList = new ArrayList<>();
        for (OrderItemPojo orderItemPojo : orderItemPojoList) {
            Double subTotal = (orderItemPojo.getSellingPrice() * orderItemPojo.getQuantity());

            InvoiceItemData invoiceItemData = new InvoiceItemData();
            invoiceItemData.setTotal(subTotal);
            invoiceItemData.setPrice(orderItemPojo.getSellingPrice());
            invoiceItemData.setQuantity(orderItemPojo.getQuantity());
            ProductPojo productPojo = productApi.getById(orderItemPojo.getProductId());
            invoiceItemData.setProductName(productPojo.getName() + "-" + productPojo.getBarcode());

            invoiceItemDataList.add(invoiceItemData);
            total += subTotal;
        }

        InvoiceData invoiceData = new InvoiceData();
        invoiceData.setTotal(total);
        invoiceData.setInvoiceItems(invoiceItemDataList);
        invoiceData.setOrderId(orderId);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss z");
        invoiceData.setOrderDateTime(orderPojo.getDateTime().format(formatter));
        invoiceData.setInvoiceDateTime(ZonedDateTime.now().format(formatter));

        String url = "http://localhost:8000/invoice_app/api/invoices/generate-invoice";
        String base64Pdf = restTemplate.postForObject(url, invoiceData, String.class);

        return base64Pdf;
    }
}