package com.increff.pos.utils;

import com.increff.pos.model.data.OrderError;
import com.increff.pos.model.form.*;
import lombok.Getter;
import lombok.Setter;

import java.text.DecimalFormat;
import java.time.ZonedDateTime;
import java.util.*;

@Getter
@Setter
public class UtilMethods {

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
        productForm.setMrp( Double.valueOf(new DecimalFormat("0.00").format( productForm.getMrp() )) );
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

    public static void normalizeOrderForm(OrderItemForm orderItemForm){
        orderItemForm.setBarcode(orderItemForm.getBarcode().trim().toLowerCase());
    }

    public static void validateOrderForm(OrderItemForm orderItemForm) throws ApiException {
        if(orderItemForm.getQuantity() <= 0){
            throw new ApiException("Quantity should be greater than 0.");
        } else if (orderItemForm.getQuantity() > Constants.MAX_INVENTORY) {
            throw new ApiException("Quantity should not exceed "+ Constants.MAX_INVENTORY);
        } else if (orderItemForm.getSellingPrice() <= 0) {
            throw new ApiException("Selling Price should be greater than 0.");
        } else if (orderItemForm.getBarcode().length() > Constants.MAX_LENGTH) {
            throw new ApiException("Barcode should not exceed "+ Constants.MAX_LENGTH+" letters");
        }
    }

    public static void normalizeInventoryForm(InventoryForm inventoryForm) {
        inventoryForm.setBarcode(inventoryForm.getBarcode().trim().toLowerCase());
    }

    public static List<OrderError> validateOrderFormList(List<OrderItemForm> orderItemFormList) {
        List<OrderError> orderErrorList = new ArrayList<>();
        Set<String> barcodes = new HashSet<>();
        int index = 0;
        for(OrderItemForm orderItemForm : orderItemFormList) {
            try{
                if(barcodes.contains(orderItemForm.getBarcode())){
                    throw new ApiException("This product has already been added to this order");
                }
                barcodes.add(orderItemForm.getBarcode());
                validateOrderForm(orderItemForm);
            }catch (ApiException apiException){
                OrderError orderError = new OrderError();
                orderError.setMessage(apiException.getMessage());
                orderError.setIndex(index);
                orderError.setBarcode(orderItemForm.getBarcode());
                orderErrorList.add(orderError);
            }
            index++;
        }
        return orderErrorList;
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
