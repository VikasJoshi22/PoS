package com.increff.pos.dto;

import com.increff.pos.dao.OrderDao;
import com.increff.pos.dao.ProductDao;
import com.increff.pos.flow.OrderFlow;
import com.increff.pos.model.data.OrderData;
import com.increff.pos.model.form.OrderForm;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.utils.ApiException;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class OrderDto {
    @Autowired
    private OrderDao orderDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private OrderFlow orderFlow;


    public void create(OrderForm orderForm) throws ApiException {
        DtoHelper.normalizeOrderForm(orderForm);
        DtoHelper.validateOrderForm(orderForm);
        OrderItemPojo orderItemPojo = DtoHelper.convertOrderFormToOrderItemPojo(orderForm); // For now, productId and orderId are empty
        orderFlow.create(orderItemPojo, orderForm.getBarcode());

        //checking if product with given barcode exists or not
//        ProductPojo product = productDao.getByBarcode(orderForm.getBarcode());
//        if(Objects.isNull(product)){
//            throw new ApiException("Product with barcode '"+orderForm.getBarcode()+"' doesn't exists");
//        }

        //checking product availability
//        InventoryPojo inventory = inventoryDao.getByProduct(product);
//        if(Objects.isNull(inventory) || (inventory.getQuantity() <= 0)) {
//            throw new ApiException("Product '" + product.getName() + "' with barcode '" + product.getBarcode() + "' is out of Stock");
//        } else if (inventory.getQuantity() < orderForm.getQuantity()) {
//            throw new ApiException("Only "+ inventory.getQuantity() + (inventory.getQuantity()==1 ? " item is" : " items are") + " in stock for product '" + product.getName() + "' with barcode '" + product.getBarcode());
//        }

        //creating OrderPojo and passing it in OrderItemPojo
//        OrderPojo order = createOrderPojo();
//        orderDao.addOrder(order);
//        orderDao.addOrderItem();

        //reducing inventory
//        inventory.setQuantity(inventory.getQuantity() - orderForm.getQuantity());
//        inventoryDao.update(inventory);
    }

    public void batchCreate(List<OrderForm> orderFormList) throws ApiException{

        //this string will contain all the issues
        StringBuilder issues = new StringBuilder();

        List<OrderItemPojo> orderItemPojoList = new ArrayList<>();
        List<String> barcodeList = new ArrayList<>();

        for(OrderForm orderForm: orderFormList) {
            try{
                DtoHelper.normalizeOrderForm(orderForm);
                DtoHelper.validateOrderForm(orderForm);
                orderItemPojoList.add(DtoHelper.convertOrderFormToOrderItemPojo(orderForm));
                barcodeList.add(orderForm.getBarcode());
            }catch (ApiException apiException){
                issues.append(apiException.getMessage()).append("\n");
            }
        }

        //if there is some issue in orderFormList, then it will throw ApiException and the whole method will be rolled back
        if(issues.length()!=0){
            throw new ApiException(issues.toString());
        }else {
            orderFlow.batchCreate(orderItemPojoList, barcodeList);
        }
    }

    @ApiOperation("get all order's detail")
    @RequestMapping("/get-all")
    public List<OrderData> getAll(){
        List<OrderPojo> orderPojoList = orderFlow.getAllOrders();
        HashMap<Integer, ZonedDateTime> orderMap = new HashMap<>();
        for(OrderPojo orderPojo: orderPojoList){
            orderMap.put(orderPojo.getId(), orderPojo.getDateTime());
        }

        List<OrderItemPojo> orderItemPojoList = orderFlow.getAllOrderItem();

        List<OrderData> orderDataList = new ArrayList<>();
        for(OrderItemPojo orderItemPojo: orderItemPojoList){
            OrderData orderData = DtoHelper.convertOrderItemPojoToOrderData(orderItemPojo);

            ZonedDateTime dateTime =  orderMap.get(orderData.getOrderId());
            orderData.setOrderPlaced(Objects.nonNull(dateTime));
            orderData.setDateTime(dateTime);

            orderDataList.add(orderData);
        }


        return orderDataList;
    }

}
