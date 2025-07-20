package com.increff.pos.dto;

import com.increff.pos.model.data.*;
import com.increff.pos.model.form.ClientForm;
import com.increff.pos.model.form.InventoryForm;
import com.increff.pos.model.form.OrderForm;
import com.increff.pos.model.form.ProductForm;
import com.increff.pos.pojo.*;
import com.increff.pos.utils.ApiException;
import com.increff.pos.utils.Constants;

import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class DtoHelper {

    private static final DecimalFormat df = new DecimalFormat("0.00");

    public static ClientPojo convertClientFormToClientPojo(ClientForm client){
        ClientPojo c = new ClientPojo();
        c.setName(client.getName().toLowerCase());
        return c;
    }

    public static ClientData convertClientPojoToClientData(ClientPojo clientPojo){
        ClientData clientData = new ClientData();
        clientData.setId(clientPojo.getId());
        clientData.setName(clientPojo.getName());
        return clientData;
    }

    public static ProductPojo convertProductFormToProductPojo(ProductForm productForm) {
        ProductPojo productPojo = new ProductPojo();
        productPojo.setBarcode(productForm.getBarcode());
        productPojo.setMrp(productForm.getMrp());
        productPojo.setImageUrl(productForm.getImageUrl());
        productPojo.setName(productForm.getName().toLowerCase());
        return productPojo;
    }

    public static ProductData convertProductPojoToProductData(ProductPojo productPojo){
        ProductData productData = new ProductData();

        productData.setBarcode(productPojo.getBarcode());
        productData.setName(productPojo.getName());
        productData.setId(productPojo.getId());
        productData.setMrp(productPojo.getMrp());
        productData.setImageUrl(productPojo.getImageUrl());

        return productData;
    }

    public static InventoryPojo convertInventoryFormToInventoryPojo(InventoryForm inventoryForm){
        InventoryPojo inventoryPojo = new InventoryPojo();
        inventoryPojo.setQuantity(inventoryForm.getQuantity());
        return inventoryPojo;
    }

    public static InventoryData convertInventoryPojoToInventoryData(InventoryPojo inventoryPojo) {
        InventoryData inventoryData = new InventoryData();
        inventoryData.setId(inventoryPojo.getId());
        inventoryData.setQuantity(inventoryPojo.getQuantity());
        return inventoryData;
    }

    public static OrderItemPojo convertOrderFormToOrderItemPojo(OrderForm orderForm){
        OrderItemPojo orderItemPojo = new OrderItemPojo();
        orderItemPojo.setSellingPrice(orderForm.getSellingPrice());
        orderItemPojo.setQuantity(orderForm.getQuantity());
        return orderItemPojo;
    }

    public static OrderItemData convertOrderItemPojoToOrderItemData(OrderItemPojo orderItemPojo) {
        OrderItemData orderItemData = new OrderItemData();

        orderItemData.setId(orderItemPojo.getId());
        orderItemData.setQuantity(orderItemPojo.getQuantity());
        orderItemData.setProductId(orderItemPojo.getProductId());
        orderItemData.setSellingPrice(orderItemPojo.getSellingPrice());


        return orderItemData;
    }

    public static void normalizeClientForm(ClientForm clientForm){
        clientForm.setName(clientForm.getName().toLowerCase().trim());
    }

    public static void validateClientForm(ClientForm clientForm) throws ApiException{
        if(Objects.isNull(clientForm) || Objects.isNull(clientForm.getName())) {
            throw new ApiException("Client should not be null");
        }else if (clientForm.getName().isEmpty()) {
            throw new ApiException("Client name should not be empty");
        } else if (clientForm.getName().length() > Constants.MAX_LENGTH) {
            throw new ApiException("Client name should not exceed "+ Constants.MAX_LENGTH+" letters");
        }
    }

    public static void validateProductForm(ProductForm productForm) throws ApiException{
        if(Objects.isNull(productForm)){
            throw new ApiException("product should not be null");
        } else if (productForm.getBarcode().length() > Constants.MAX_LENGTH) {
            throw new ApiException("Barcode should not exceed "+ Constants.MAX_LENGTH+" letters");
        } else if (productForm.getName().length() > Constants.MAX_LENGTH) {
            throw new ApiException("Product name should not exceed "+ Constants.MAX_LENGTH+" letters");
        } else if (productForm.getImageUrl().length() > 500) {
            throw new ApiException("Barcode should not exceed 500 letters");
        } else if (productForm.getMrp() <= 0.0) {
            throw new ApiException("mrp should be greater then 0");
        }
    }

    public static void normalizeProductForm(ProductForm productForm){
        productForm.setMrp( Double.valueOf(df.format( productForm.getMrp() )) );
        productForm.setName(productForm.getName().trim().toLowerCase());
        productForm.setBarcode(productForm.getBarcode().trim().toLowerCase());
        productForm.setImageUrl(productForm.getImageUrl().trim());
        productForm.setClientName(productForm.getClientName().trim().toLowerCase());
    }

    public static void validateInventoryForm(InventoryForm inventoryForm) throws ApiException{
        if(inventoryForm.getQuantity() <= 0){
            throw new ApiException("Quantity should be greater than 0");
        } else if(inventoryForm.getBarcode().length() > Constants.MAX_LENGTH){
            throw new ApiException("barcode should not be greater then 50 letters");
        }
    }

    public static void normalizeOrderForm(OrderForm orderForm){
        orderForm.setBarcode(orderForm.getBarcode().trim().toLowerCase());
    }

    public static void validateOrderForm(OrderForm orderForm) throws ApiException {
        if(orderForm.getQuantity() <= 0){
            throw new ApiException("Quantity should be greater than 0.");
        } else if (orderForm.getSellingPrice() <= 0) {
            throw new ApiException("Selling Price should be greater than 0.");
        } else if (orderForm.getBarcode().length() > Constants.MAX_LENGTH) {
            throw new ApiException("Barcode should not exceed "+ Constants.MAX_LENGTH+" letters");
        }
    }

    public static ProductForm convertProductPojoToProductForm(ProductPojo productPojo){
        ProductForm productForm = new ProductForm();

        productForm.setImageUrl(productPojo.getImageUrl());
        productForm.setName(productPojo.getName());
        productForm.setMrp(productPojo.getMrp());
        productForm.setBarcode(productPojo.getBarcode());

        return productForm;
    }

    public static DailySalesReportData convertDailySalesReportPojoToData(DailySalesReportPojo dailySalesReportPojo) {
        DailySalesReportData dailySalesReportData = new DailySalesReportData();
        dailySalesReportData.setDate(dailySalesReportPojo.getDateTime().format(DateTimeFormatter.ISO_DATE));
        dailySalesReportData.setTotalRevenue(dailySalesReportPojo.getTotalRevenue());
        dailySalesReportData.setInvoicedItemsCount(dailySalesReportPojo.getInvoicedItemsCount());
        dailySalesReportData.setInvoicedOrdersCount(dailySalesReportPojo.getInvoicedOrdersCount());
        return dailySalesReportData;
    }
}
