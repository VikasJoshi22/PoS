package com.increff.pos.dto;

import com.increff.pos.dao.OrderDao;
import com.increff.pos.dao.ProductDao;
import com.increff.pos.flow.OrderFlow;
import com.increff.pos.model.data.OrderData;
import com.increff.pos.model.data.OrderError;
import com.increff.pos.model.data.OrderItemData;
import com.increff.pos.model.form.OrderForm;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.utils.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
public class OrderDto {
    @Autowired
    private OrderDao orderDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private OrderFlow orderFlow;

    public List<OrderError> create(List<OrderForm> orderFormList) throws ApiException{

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

        orderErrorList.addAll(orderFlow.create(orderItemPojoList, barcodeList));
        return orderErrorList;
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
        ZonedDateTime dateTime = orderPojo.getDateTime();
        orderData.setDateTime(dateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        orderData.setOrderItems(orderItemDataList);
        return orderData;
    }

    public List<OrderData> getAllOrderDetails() throws ApiException {
        List<OrderData> orderDataList = new ArrayList<>();
        List<OrderPojo> orderPojoList = orderFlow.getAllOrders();
        for(OrderPojo orderPojo: orderPojoList){
            OrderData orderData = getOrderDetails(orderPojo.getId());
            orderDataList.add(orderData);
        }
        return orderDataList;
    }

//    @ApiOperation("get all order's detail")
//    @RequestMapping("/get-all")
//    public List<OrderData> getAll(){
//        List<OrderPojo> orderPojoList = orderFlow.getAllOrders();
//        HashMap<Integer, ZonedDateTime> orderMap = new HashMap<>();
//        for(OrderPojo orderPojo: orderPojoList){
//            orderMap.put(orderPojo.getId(), orderPojo.getDateTime());
//        }
//
//        List<OrderItemPojo> orderItemPojoList = orderFlow.getAllOrderItem();
//
//        List<OrderData> orderDataList = new ArrayList<>();
//        for(OrderItemPojo orderItemPojo: orderItemPojoList){
//            OrderData orderData = DtoHelper.convertOrderItemPojoToOrderData(orderItemPojo);
//
//            ZonedDateTime dateTime =  orderMap.get(orderData.getOrderId());
//            orderData.setOrderPlaced(Objects.nonNull(dateTime));
//            orderData.setDateTime(dateTime);
//
//            orderDataList.add(orderData);
//        }
//
//
//        return orderDataList;
//    }

}
