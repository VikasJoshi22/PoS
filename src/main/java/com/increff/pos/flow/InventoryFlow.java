package com.increff.pos.flow;

import com.increff.pos.api.InventoryApi;
import com.increff.pos.api.ProductApi;
import com.increff.pos.model.data.OperationResponse;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.utils.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.media.jai.operator.ErodeDescriptor;
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

    public List<OperationResponse<InventoryPojo>> batchAdd(List<InventoryPojo> inventoryPojoList){
        List<OperationResponse<InventoryPojo>> operationResponseList = new ArrayList<>();

        boolean errorOccured = false;
        for(InventoryPojo inventoryPojo: inventoryPojoList){
            OperationResponse<InventoryPojo> operationResponse = new OperationResponse<>();
            operationResponse.setData(inventoryPojo);
            operationResponse.setMessage("No error");
            try{
                doesProductExists(inventoryPojo.getProductId());
            } catch (ApiException e) {
                errorOccured = true;
                operationResponse.setMessage(e.getMessage());
            }
            operationResponseList.add(operationResponse);
        }

        // if there is any error then we'll not add any inventory
        if(!errorOccured){
            inventoryApi.batchAdd(inventoryPojoList);
        }
        return operationResponseList;
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
