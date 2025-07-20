package com.increff.pos.dto;

import com.increff.pos.api.OrderApi;
import com.increff.pos.dao.ProductDao;
import com.increff.pos.flow.OrderFlow;
import com.increff.pos.model.data.ErrorData;
import com.increff.pos.model.data.OrderData;
import com.increff.pos.model.data.OrderError;
import com.increff.pos.model.data.OrderItemData;
import com.increff.pos.model.form.OrderFilters;
import com.increff.pos.model.form.OrderForm;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.utils.ApiException;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
public class OrderDto {
    @Autowired
    private OrderApi orderApi;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private OrderFlow orderFlow;

    public ErrorData<OrderError> create(List<OrderForm> orderFormList){

        List<OrderError> orderErrorList = new ArrayList<>();
        List<OrderItemPojo> orderItemPojoList = new ArrayList<>();
        List<String> barcodeList = new ArrayList<>();

        //checking for duplicate barcodes in input
        Set<String> barcodes = new HashSet<>();

        int index = 0;
        for(OrderForm orderForm: orderFormList) {

            try{
                orderItemPojoList.add(DtoHelper.convertOrderFormToOrderItemPojo(orderForm));
                barcodeList.add(orderForm.getBarcode());

                //checking if barcode already exists
                if(barcodes.contains(orderForm.getBarcode())){
                    throw new ApiException("this product has already been added to this order");
                }
                barcodes.add(orderForm.getBarcode());
                DtoHelper.normalizeOrderForm(orderForm);
                DtoHelper.validateOrderForm(orderForm);
            }catch (ApiException apiException){
                OrderError orderError = new OrderError();
                orderError.setMessage(apiException.getMessage());
                orderError.setIndex(index);
                orderError.setBarcode(orderForm.getBarcode());

                orderErrorList.add(orderError);
            }
            index++;
        }

        ErrorData<OrderError> orderErrorData = orderFlow.create(orderItemPojoList, barcodeList);
        orderErrorList.addAll(orderErrorData.getErrorList());
        orderErrorData.setErrorList(orderErrorList);
        return orderErrorData;
    }

    public OrderData getOrderDetails(Integer orderId) throws ApiException {
        OrderData orderData = new OrderData();

        OrderPojo orderPojo = orderFlow.getOrderById(orderId);
        List<OrderItemPojo> orderItemPojoList = orderFlow.getOrderItemsByOrderId(orderId);
        List<OrderItemData> orderItemDataList = new ArrayList<>();
        for(OrderItemPojo orderItemPojo: orderItemPojoList){
            OrderItemData orderItemData = DtoHelper.convertOrderItemPojoToOrderItemData(orderItemPojo);
            orderItemDataList.add(orderItemData);
        }

        //making OrderData out of OrderPojo and OrderItemPojo
        orderData.setId(orderId);
        orderData.setStatus(orderPojo.getStatus());
        ZonedDateTime dateTime = orderPojo.getDateTime();
        orderData.setDateTime(dateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        orderData.setOrderItems(orderItemDataList);
        return orderData;
    }

//    public List<OrderData> getAllOrderDetails() throws ApiException {
//        List<OrderData> orderDataList = new ArrayList<>();
//        List<OrderPojo> orderPojoList = orderFlow.getAllOrders();
//        for(OrderPojo orderPojo: orderPojoList){
//            OrderData orderData = getOrderDetails(orderPojo.getId());
//            orderDataList.add(orderData);
//        }
//        return orderDataList;
//    }

    public void makeOrderInvoiced(Integer id) throws ApiException {
        orderApi.makeOrderInvoiced(id);
    }

    public List<OrderData> getAll(OrderFilters orderFilters) throws ApiException{
        List<OrderPojo> orderPojoList = orderFlow.getAllOrders(orderFilters);
        List<OrderData> orderDataList = new ArrayList<>();
        for(OrderPojo orderPojo: orderPojoList){
            OrderData orderData = new OrderData();
            orderData.setId(orderPojo.getId());
            orderData.setDateTime(orderPojo.getDateTime().format(DateTimeFormatter.ISO_DATE_TIME));
            orderData.setStatus(orderPojo.getStatus());

            List<OrderItemPojo> orderItemPojoList = orderFlow.getOrderItemsByOrderId(orderPojo.getId());
            List<OrderItemData> orderItemDataList = new ArrayList<>();
            for(OrderItemPojo orderItemPojo: orderItemPojoList){
                orderItemDataList.add(DtoHelper.convertOrderItemPojoToOrderItemData(orderItemPojo));
            }
            orderData.setOrderItems(orderItemDataList);
            orderDataList.add(orderData);
        }
        return orderDataList;
    }

    public Long getTotalCount(OrderFilters orderFilters) {
        return orderApi.getTotalCount(orderFilters);
    }
}
