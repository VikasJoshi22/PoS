package com.increff.pos.flow;

import com.increff.pos.api.InventoryApi;
import com.increff.pos.api.ProductApi;
import com.increff.pos.model.form.InventoryForm;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.utils.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Component
@Transactional(rollbackFor = ApiException.class)
public class InventoryFlow {
    @Autowired
    private ProductApi productApi;
    @Autowired
    private InventoryApi inventoryApi;

    public void update(InventoryPojo inventoryPojo) throws ApiException{
        doesProductExists(inventoryPojo.getProductId());
        inventoryApi.update(inventoryPojo);
    }

    public void batchUpdate(List<InventoryPojo> inventoryPojoList) throws ApiException {
        StringBuilder failuremessage = new StringBuilder();
        int row = 1;
        for(InventoryPojo inventoryPojo: inventoryPojoList){
            try{
                doesProductExists(inventoryPojo.getProductId());
            } catch (ApiException e) {
                failuremessage.append("row "+row+": "+ e.getMessage()+"\n ");
            }
            row++;
        }
        if(failuremessage.length() > 0){
            throw new ApiException(failuremessage.toString());
        }
        inventoryApi.batchUpdate(inventoryPojoList);
    }


    private void doesProductExists(Integer id) throws ApiException{
        ProductPojo product = productApi.getById(id);
        if(Objects.isNull(product)){
            throw new ApiException("product doesn't exists");
        }
    }
}
