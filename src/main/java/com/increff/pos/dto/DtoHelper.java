package com.increff.pos.dto;

import com.increff.pos.model.data.*;
import com.increff.pos.model.form.*;
import com.increff.pos.pojo.*;
import com.increff.pos.utils.ApiException;
import com.increff.pos.utils.Constants;
import com.sun.xml.bind.v2.runtime.reflect.opt.Const;

import java.text.DecimalFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class DtoHelper {

    private static final DecimalFormat df = new DecimalFormat("0.00");

    public static ClientPojo convertClientFormToClientPojo(ClientForm client){
        ClientPojo c = new ClientPojo();
        c.setName(client.getName());
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
            throw new ApiException("Product should not be null");
        } else if (productForm.getBarcode().length() > Constants.MAX_LENGTH) {
            throw new ApiException("Barcode should not exceed "+ Constants.MAX_LENGTH+" letters");
        } else if (productForm.getName().length() > Constants.MAX_LENGTH) {
            throw new ApiException("Product name should not exceed "+ Constants.MAX_LENGTH+" letters");
        } else if (productForm.getImageUrl().length() > Constants.MAX_URL_LENGTH) {
            throw new ApiException("Barcode should not exceed "+Constants.MAX_URL_LENGTH+" letters");
        } else if (productForm.getMrp() <= 0.0) {
            throw new ApiException("Mrp should be greater then 0");
        } else if (productForm.getMrp() > Constants.MAX_MRP) {
            throw new ApiException("Mrp should not exceed ₹"+Constants.MAX_MRP);
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
        } else if (inventoryForm.getQuantity() > Constants.MAX_INVENTORY) {
            throw new ApiException("Quantity should not exceed "+Constants.MAX_INVENTORY);
        } else if(inventoryForm.getBarcode().length() > Constants.MAX_LENGTH){
            throw new ApiException("Barcode should not be greater then "+Constants.MAX_LENGTH+" letters");
        }
    }

    public static void normalizeOrderForm(OrderForm orderForm){
        orderForm.setBarcode(orderForm.getBarcode().trim().toLowerCase());
    }

    public static void validateOrderForm(OrderForm orderForm) throws ApiException {
        if(orderForm.getQuantity() <= 0){
            throw new ApiException("Quantity should be greater than 0.");
        } else if (orderForm.getQuantity() > Constants.MAX_INVENTORY) {
            throw new ApiException("Quantity should not exceed "+ Constants.MAX_INVENTORY);
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

    public static List<ClientData> convertClientPojoListToClientDataList(List<ClientPojo> clientPojoList) {
        List<ClientData> clientDataList = new ArrayList<>();
        for (ClientPojo clientPojo : clientPojoList) {
            ClientData clientData = convertClientPojoToClientData(clientPojo);
            clientDataList.add(clientData);
        }
        return clientDataList;
    }

    public static void normalizeInventoryForm(InventoryForm inventoryForm) {
        inventoryForm.setBarcode(inventoryForm.getBarcode().trim().toLowerCase());
    }

    public static List<OrderItemPojo> convertOrderFormListToOrderItemPojoList(List<OrderForm> orderFormList) {
        List<OrderItemPojo> orderItemPojoList = new ArrayList<>();
        for(OrderForm orderForm: orderFormList){
            normalizeOrderForm(orderForm);
            orderItemPojoList.add(convertOrderFormToOrderItemPojo(orderForm));
        }
        return orderItemPojoList;
    }

    public static List<OrderError> validateOrderFormList(List<OrderForm> orderFormList) {
        List<OrderError> orderErrorList = new ArrayList<>();
        Set<String> barcodes = new HashSet<>();
        int index = 0;
        for(OrderForm orderForm: orderFormList) {
            try{
                if(barcodes.contains(orderForm.getBarcode())){
                    throw new ApiException("This product has already been added to this order");
                }
                barcodes.add(orderForm.getBarcode());
                validateOrderForm(orderForm);
            }catch (ApiException apiException){
                OrderError orderError = new OrderError();
                orderError.setMessage(apiException.getMessage());
                orderError.setIndex(index);
                orderError.setBarcode(orderForm.getBarcode());
                orderErrorList.add(orderError);
            }
            index++;
        }
        return orderErrorList;
    }

    public static List<OrderItemData> convertOrderItemPojoListToOrderItemDataList(List<OrderItemPojo> orderItemPojoList) {
        List<OrderItemData> orderItemDataList = new ArrayList<>();
        for(OrderItemPojo orderItemPojo: orderItemPojoList){
            OrderItemData orderItemData = convertOrderItemPojoToOrderItemData(orderItemPojo);
            orderItemDataList.add(orderItemData);
        }
        return orderItemDataList;
    }

    public static OrderData convertToOrderData(OrderPojo orderPojo, List<OrderItemData> orderItemDataList) {
        OrderData orderData = new OrderData();
        orderData.setId(orderPojo.getId());
        orderData.setStatus(orderPojo.getStatus());
        ZonedDateTime dateTime = orderPojo.getDateTime();
        orderData.setDateTime(dateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        orderData.setOrderItems(orderItemDataList);
        return orderData;
    }

    public static List<DailySalesReportData> convertDailySalesReportPojoListToDataList(List<DailySalesReportPojo> dailySalesReportPojoList) {
        List<DailySalesReportData> dailySalesReportDataList = new ArrayList<>();
        for(DailySalesReportPojo dailySalesReportPojo: dailySalesReportPojoList){
            DailySalesReportData dailySalesReportData = DtoHelper.convertDailySalesReportPojoToData(dailySalesReportPojo);
            dailySalesReportDataList.add(dailySalesReportData);
        }
        return dailySalesReportDataList;
    }

    public static void normalizeSalesReportForm(SalesReportForm salesReportForm) {
        salesReportForm.setClient(salesReportForm.getClient().toLowerCase());
        salesReportForm.setProductBarcode(salesReportForm.getProductBarcode().toLowerCase());
    }

    public static ZonedDateTime parseStartDate(String startDate) {
        if(startDate.isEmpty()){
            return ZonedDateTime.parse(Constants.MIN_DATE);
        } else {
            return ZonedDateTime.parse(startDate);
        }
    }

    public static ZonedDateTime parseEndDate(String endDate) {
        if(endDate.isEmpty()){
            return ZonedDateTime.now();
        } else {
            return ZonedDateTime.parse(endDate);
        }
    }
}
