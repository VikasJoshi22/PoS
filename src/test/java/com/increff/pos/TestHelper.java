package com.increff.pos;

import com.increff.pos.model.form.InventoryForm;
import com.increff.pos.model.form.ProductForm;
import com.increff.pos.model.form.ClientForm;
import com.increff.pos.model.form.OrderForm;
import com.increff.pos.model.form.OrderFilters;
import com.increff.pos.model.form.DailySalesReportForm;
import com.increff.pos.model.form.SalesReportForm;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.pojo.ClientPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.DailySalesReportPojo;
import com.increff.pos.model.data.SalesReportData;

import java.time.ZonedDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class TestHelper {
    
    public static InventoryForm createInventoryForm(String barcode, Integer quantity) {
        InventoryForm form = new InventoryForm();
        form.setBarcode(barcode);
        form.setQuantity(quantity);
        return form;
    }
    
    public static ProductForm createProductForm(String barcode, String clientName, String name, Double mrp, String imageUrl) {
        ProductForm form = new ProductForm();
        form.setBarcode(barcode);
        form.setClientName(clientName);
        form.setName(name);
        form.setMrp(mrp);
        form.setImageUrl(imageUrl);
        return form;
    }
    
    public static ClientForm createClientForm(String name) {
        ClientForm form = new ClientForm();
        form.setName(name);
        return form;
    }
    
    public static OrderForm createOrderForm(String barcode, Integer quantity, Double sellingPrice) {
        OrderForm form = new OrderForm();
        form.setBarcode(barcode);
        form.setQuantity(quantity);
        form.setSellingPrice(sellingPrice);
        return form;
    }
    
    public static List<OrderForm> createOrderFormList(String barcode, Integer quantity, Double sellingPrice) {
        List<OrderForm> list = new ArrayList<>();
        list.add(createOrderForm(barcode, quantity, sellingPrice));
        return list;
    }
    
    public static OrderFilters createOrderFilters(Integer page, Integer size, String startDate, String endDate) {
        OrderFilters filters = new OrderFilters();
        filters.setPage(page);
        filters.setSize(size);
        filters.setStartDate(startDate);
        filters.setEndDate(endDate);
        return filters;
    }
    
    public static DailySalesReportForm createDailySalesReportForm(String startDate, String endDate, Integer page, Integer size) {
        DailySalesReportForm form = new DailySalesReportForm();
        form.setStartDate(startDate);
        form.setEndDate(endDate);
        form.setPage(page);
        form.setSize(size);
        return form;
    }
    
    public static SalesReportForm createSalesReportForm(String startDate, String endDate, String client, String productBarcode) {
        SalesReportForm form = new SalesReportForm();
        form.setStartDate(startDate);
        form.setEndDate(endDate);
        form.setClient(client);
        form.setProductBarcode(productBarcode);
        return form;
    }
    
    public static InventoryPojo createInventoryPojo(Integer productId, Integer quantity) {
        InventoryPojo pojo = new InventoryPojo();
        pojo.setProductId(productId);
        pojo.setQuantity(quantity);
        return pojo;
    }
    
    public static ProductPojo createProductPojo(String barcode, Integer clientId, String name, Double mrp, String imageUrl) {
        ProductPojo pojo = new ProductPojo();
        pojo.setBarcode(barcode);
        pojo.setClientId(clientId);
        pojo.setName(name);
        pojo.setMrp(mrp);
        pojo.setImageUrl(imageUrl);
        return pojo;
    }
    
    public static ClientPojo createClientPojo(String name) {
        ClientPojo pojo = new ClientPojo();
        pojo.setName(name);
        return pojo;
    }
    
    public static OrderPojo createOrderPojo() {
        OrderPojo pojo = new OrderPojo();
        pojo.setDateTime(ZonedDateTime.now(ZoneId.of("UTC")));
        pojo.setStatus("created");
        return pojo;
    }
    
    public static OrderPojo createOrderPojo(ZonedDateTime dateTime, String status) {
        OrderPojo pojo = new OrderPojo();
        pojo.setDateTime(dateTime);
        pojo.setStatus(status);
        return pojo;
    }
    
    public static OrderItemPojo createOrderItemPojo(Integer orderId, Integer productId, Integer quantity, Double sellingPrice) {
        OrderItemPojo pojo = new OrderItemPojo();
        pojo.setOrderId(orderId);
        pojo.setProductId(productId);
        pojo.setQuantity(quantity);
        pojo.setSellingPrice(sellingPrice);
        return pojo;
    }
    
    public static OrderItemPojo createOrderItemPojo(Integer productId, Integer quantity, Double sellingPrice) {
        OrderItemPojo pojo = new OrderItemPojo();
        pojo.setProductId(productId);
        pojo.setQuantity(quantity);
        pojo.setSellingPrice(sellingPrice);
        return pojo;
    }
    
    public static DailySalesReportPojo createDailySalesReportPojo(ZonedDateTime dateTime, Integer invoicedOrdersCount, Integer invoicedItemsCount, Double totalRevenue) {
        DailySalesReportPojo pojo = new DailySalesReportPojo();
        pojo.setDateTime(dateTime);
        pojo.setInvoicedOrdersCount(invoicedOrdersCount);
        pojo.setInvoicedItemsCount(invoicedItemsCount);
        pojo.setTotalRevenue(totalRevenue);
        return pojo;
    }
    
    public static SalesReportData createSalesReportData(String client, String productBarcode, Integer quantity, Double revenue) {
        SalesReportData data = new SalesReportData();
        data.setClient(client);
        data.setProductBarcode(productBarcode);
        data.setQuantity(quantity);
        data.setRevenue(revenue);
        return data;
    }
}
