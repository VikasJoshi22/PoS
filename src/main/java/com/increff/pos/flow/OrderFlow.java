package com.increff.pos.flow;

import com.increff.pos.api.InventoryApi;
import com.increff.pos.api.OrderApi;
import com.increff.pos.api.OrderItemApi;
import com.increff.pos.api.ProductApi;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.utils.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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

    public void create(OrderItemPojo orderItemPojo, String barcode) throws ApiException{
        //checking if product exists or not
        ProductPojo productPojo = productApi.getByBarcode(barcode);
        if(Objects.isNull(productPojo)){
            throw new ApiException("Product with barcode '"+barcode+"' doesn't exists");
        }else {
            orderItemPojo.setProductId(productPojo.getId());
        }

        //checking product availability
        InventoryPojo inventory = inventoryApi.getByProductId(productPojo.getId());
        if(Objects.isNull(inventory) || (inventory.getQuantity() <= 0)) {
            throw new ApiException("Product '" + productPojo.getName() + "' with barcode '" + productPojo.getBarcode() + "' is out of Stock");
        } else if (inventory.getQuantity() < orderItemPojo.getQuantity()) {
            throw new ApiException("Only "+ inventory.getQuantity() + (inventory.getQuantity()==1 ? " item is" : " items are") + " in stock for product '" + productPojo.getName() + "' with barcode '" + productPojo.getBarcode());
        }

        //creating OrderPojo and setting it's id in OrderItemPojo
        OrderPojo orderPojo = new OrderPojo();
        orderApi.addOrder(orderPojo);
        orderItemPojo.setOrderId(orderPojo.getId());
        orderItemApi.addOrderItem(orderItemPojo);

        //reducing inventory
        inventory.setQuantity(inventory.getQuantity() - orderItemPojo.getQuantity());
        inventoryApi.update(inventory);

    }

    public void batchCreate(List<OrderItemPojo> orderItemPojoList, List<String> barcodeList) throws ApiException{
        //this string will contain all the issues
        StringBuilder issues = new StringBuilder();

        // OrderPojo will be same for every item in bulk order
        OrderPojo order = new OrderPojo();
        orderApi.addOrder(order);

        int i = 0;
        for(OrderItemPojo orderItemPojo: orderItemPojoList) {
            try{
                //checking if product with given barcode exists or not
                ProductPojo product = productApi.getByBarcode(barcodeList.get(i));
                if(Objects.isNull(product)){
                    throw new ApiException("Product with barcode '"+barcodeList.get(i)+"' doesn't exists");
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
                inventoryApi.update(inventory);

            }catch (ApiException apiException){
                issues.append(apiException.getMessage()).append("\n");
            }
            i++;
        }

        //if there is some issue in orderFormList, then it will throw ApiException and the whole method will be rolled back
        if(issues.length()!=0){
            throw new ApiException(issues.toString());
        }
    }

    public List<OrderItemPojo> getAllOrderItem(){
        return orderItemApi.getAllOrderItems();
    }

    public List<OrderPojo> getAllOrders(){
        return orderApi.getAllOrders();
    }
}
