package com.increff.pos.flow;

import com.increff.pos.api.InventoryApi;
import com.increff.pos.api.ProductApi;
import com.increff.pos.model.data.ErrorResponse;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.utils.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@Transactional(rollbackFor = ApiException.class)
public class InventoryFlow {
    @Autowired
    private ProductApi productApi;
    @Autowired
    private InventoryApi inventoryApi;

    public void add(InventoryPojo inventoryPojo) throws ApiException{
        doesProductExists(inventoryPojo.getProductId());
        inventoryApi.add(inventoryPojo);
    }

    public List<ErrorResponse<InventoryPojo>> batchAdd(List<InventoryPojo> inventoryPojoList){
        List<ErrorResponse<InventoryPojo>> errorResponseList = new ArrayList<>();

        for(InventoryPojo inventoryPojo: inventoryPojoList){
            try{
                doesProductExists(inventoryPojo.getProductId());
            } catch (ApiException e) {
                ErrorResponse<InventoryPojo> errorResponse = new ErrorResponse<>();
                errorResponse.setMessage(e.getMessage()+"\n");
                errorResponse.setData(inventoryPojo);
                errorResponseList.add(errorResponse);
            }
        }
        inventoryApi.batchAdd(inventoryPojoList);
        return errorResponseList;
    }

    public void edit(InventoryPojo inventoryPojo) throws ApiException {
        doesProductExists(inventoryPojo.getProductId());
        inventoryApi.edit(inventoryPojo);
    }

    private void doesProductExists(Integer id) throws ApiException{
        ProductPojo product = productApi.getById(id);
        if(Objects.isNull(product)){
            throw new ApiException("product doesn't exists");
        }
    }

}
