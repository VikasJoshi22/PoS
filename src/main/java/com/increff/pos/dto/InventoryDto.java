package com.increff.pos.dto;

import com.increff.pos.api.InventoryApi;
import com.increff.pos.flow.InventoryFlow;
import com.increff.pos.model.data.InventoryData;
import com.increff.pos.model.form.InventoryForm;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.utils.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class InventoryDto {
    @Autowired
    private InventoryApi inventoryApi;

    @Autowired
    private InventoryFlow inventoryFlow;

    public void update(InventoryForm inventoryForm) throws ApiException {
        DtoHelper.validateInventoryForm(inventoryForm);
        InventoryPojo inventoryPojo = DtoHelper.convertInventoryFormToInventoryPojo(inventoryForm);
        inventoryFlow.update(inventoryPojo);
    }

    public void batchUpdate(List<InventoryForm> inventoryFormList) throws ApiException{
        StringBuilder failures = new StringBuilder();
        int row = 1;
        List<InventoryPojo> inventoryPojoList = new ArrayList<>();
        for(InventoryForm inventoryForm: inventoryFormList){
            try{
                DtoHelper.validateInventoryForm(inventoryForm);
            }catch (ApiException e){
                failures.append("Row "+row+": "+e.getMessage()+".\n");
            }
            row++;
            InventoryPojo inventoryPojo = DtoHelper.convertInventoryFormToInventoryPojo(inventoryForm);
            inventoryPojoList.add(inventoryPojo);
        }
        if(failures.length() != 0){
            throw new ApiException(failures.toString());
        }
        inventoryFlow.batchUpdate(inventoryPojoList);
    }

    public List<InventoryData> getAll(){
        List<InventoryPojo> inventoryPojoList = inventoryApi.getAll();
        List<InventoryData> inventoryDataList = new ArrayList<>();
        for(InventoryPojo inventoryPojo: inventoryPojoList){
            inventoryDataList.add(DtoHelper.convertInventoryPojoToInventoryData(inventoryPojo));
        }
        return inventoryDataList;
    }
}
