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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class OrderDto {
    @Autowired
    private OrderApi orderApi;

    @Autowired
    private OrderFlow orderFlow;

    public ErrorData<OrderError> create(List<OrderForm> orderFormList){

        List<OrderError> orderErrorList = DtoHelper.validateOrderFormList(orderFormList);
        List<OrderItemPojo> orderItemPojoList = DtoHelper.convertOrderFormListToOrderItemPojoList(orderFormList);
        List<String> barcodeList = orderFormList.stream().map(OrderForm::getBarcode).collect(Collectors.toList());
        if(!orderErrorList.isEmpty()){
            ErrorData<OrderError> errorData = new ErrorData<>();
            errorData.setErrorList(orderErrorList);
            return errorData;
        }
        ErrorData<OrderError> orderErrorData = orderFlow.create(orderItemPojoList, barcodeList);
        return orderErrorData;
    }

    public OrderData getOrderDetails(Integer orderId) throws ApiException {
        OrderPojo orderPojo = orderFlow.getOrderById(orderId);
        List<OrderItemPojo> orderItemPojoList = orderFlow.getOrderItemsByOrderId(orderId);
        List<OrderItemData> orderItemDataList = DtoHelper.convertOrderItemPojoListToOrderItemDataList(orderItemPojoList);
        return DtoHelper.convertToOrderData(orderPojo, orderItemDataList);
    }

    public void makeOrderInvoiced(Integer id) throws ApiException {
        orderApi.makeOrderInvoiced(id);
    }

    public List<OrderData> getAll(OrderFilters orderFilters) throws ApiException{
        List<OrderPojo> orderPojoList = orderFlow.getAllOrders(orderFilters);
        List<OrderData> orderDataList = new ArrayList<>();
        for(OrderPojo orderPojo: orderPojoList){
            List<OrderItemPojo> orderItemPojoList = orderFlow.getOrderItemsByOrderId(orderPojo.getId());
            List<OrderItemData> orderItemDataList = DtoHelper.convertOrderItemPojoListToOrderItemDataList(orderItemPojoList);
            orderDataList.add(DtoHelper.convertToOrderData(orderPojo, orderItemDataList));
        }
        return orderDataList;
    }

    public Long getTotalCount(OrderFilters orderFilters) {
        return orderApi.getTotalCount(orderFilters);
    }
}
