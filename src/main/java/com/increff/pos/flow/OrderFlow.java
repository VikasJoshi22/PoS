package com.increff.pos.flow;

import com.increff.pos.api.InventoryApi;
import com.increff.pos.api.OrderApi;
import com.increff.pos.api.OrderItemApi;
import com.increff.pos.api.ProductApi;
import com.increff.pos.model.data.ErrorData;
import com.increff.pos.model.data.OrderError;
import com.increff.pos.model.form.OrderFilters;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.utils.ApiException;
import com.sun.org.apache.xpath.internal.operations.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@Transactional(rollbackFor = ApiException.class)
public class OrderFlow {
    @Autowired
    private ProductApi productApi;
    @Autowired
    private InventoryApi inventoryApi;
    @Autowired
    private OrderApi orderApi;
    @Autowired
    private OrderItemApi orderItemApi;

    public ErrorData<OrderError> create(List<OrderItemPojo> orderItemPojoList, List<String> barcodeList){
        List<OrderError> orderErrorList = new ArrayList<>();

        // OrderPojo will be same for every item in bulk order
        OrderPojo order = new OrderPojo();
        order.setDateTime(ZonedDateTime.now(ZoneId.of("UTC")));
        Integer orderId = orderApi.addOrder(order);

        int i = 0;
        for(OrderItemPojo orderItemPojo: orderItemPojoList) {
            try{
                //checking if product with given barcode exists or not
                ProductPojo product = productApi.getByBarcode(barcodeList.get(i));
                if(Objects.isNull(product)){
                    throw new ApiException("Product with barcode '"+barcodeList.get(i)+"' doesn't exists");
                } else if (product.getMrp() < orderItemPojo.getSellingPrice()) {
                    throw new ApiException("Selling Price of Product is Higher than MRP");
                } else{
                    orderItemPojo.setProductId(product.getId());
                }

                //checking product availability
                InventoryPojo inventory = inventoryApi.getByProductId(product.getId());
                if(Objects.isNull(inventory) || (inventory.getQuantity() <= 0)) {
                    throw new ApiException("Product '" + product.getName() + "' with barcode '" + product.getBarcode() + "' is out of Stock");
                } else if (inventory.getQuantity() < orderItemPojo.getQuantity()) {
                    throw new ApiException("Only "+ inventory.getQuantity() + (inventory.getQuantity()==1 ? " item is" : " items are") + " in stock for product '" + product.getName() + "' with barcode '" + product.getBarcode());
                }

                orderItemPojo.setOrderId(order.getId());
                orderItemApi.addOrderItem(orderItemPojo);

                //reducing inventory
                inventory.setQuantity(inventory.getQuantity() - orderItemPojo.getQuantity());
                inventoryApi.edit(inventory);

            }catch (ApiException apiException){
                OrderError orderError = new OrderError();
                orderError.setBarcode(barcodeList.get(i));
                orderError.setIndex(i);
                orderError.setMessage(apiException.getMessage());
                orderErrorList.add(orderError);
            }
            i++;
        }
        if(!orderErrorList.isEmpty()){
            //if there is some issue in orderFormList, then it will throw ApiException and the whole method will be rolled back
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }

        ErrorData<OrderError> orderErrorData = new ErrorData<>();
        orderErrorData.setErrorList(orderErrorList);
        orderErrorData.setId(orderId);
        return orderErrorData;
    }

    public List<OrderItemPojo> getAllOrderItem(){
        return orderItemApi.getAllOrderItems();
    }

    public List<OrderItemPojo> getOrderItemsByOrderId(Integer orderId){
        return orderItemApi.getByOrderId(orderId);
    }

    public List<OrderPojo> getAllOrders(OrderFilters orderFilters) throws ApiException{
        return orderApi.getAllOrders(orderFilters);
    }

    public OrderPojo getOrderById(Integer orderId) throws ApiException {
        return orderApi.getById(orderId);
    }
}
