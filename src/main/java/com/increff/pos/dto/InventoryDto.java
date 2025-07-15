package com.increff.pos.dto;

import com.increff.pos.api.InventoryApi;
import com.increff.pos.flow.InventoryFlow;
import com.increff.pos.model.data.OperationResponse;
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

    public void add(InventoryForm inventoryForm) throws ApiException {
        DtoHelper.validateInventoryForm(inventoryForm);
        InventoryPojo inventoryPojo = DtoHelper.convertInventoryFormToInventoryPojo(inventoryForm);
        inventoryFlow.add(inventoryPojo);
    }

    public List<OperationResponse<InventoryForm>> batchAdd(List<InventoryForm> inventoryFormList){
        List<InventoryPojo> inventoryPojoList = new ArrayList<>();
        List<OperationResponse<InventoryForm>> operationResponseList = new ArrayList<>();

        boolean errorOccured = false;
        for(InventoryForm inventoryForm: inventoryFormList){
            OperationResponse<InventoryForm> operationResponse = new OperationResponse<>();
            operationResponse.setData(inventoryForm);
            operationResponse.setMessage("No error");
            try{
                DtoHelper.validateInventoryForm(inventoryForm);
            }catch (ApiException e){
                operationResponse.setMessage(e.getMessage());
                errorOccured = true;
            }
            InventoryPojo inventoryPojo = DtoHelper.convertInventoryFormToInventoryPojo(inventoryForm);
            inventoryPojoList.add(inventoryPojo);
            operationResponseList.add(operationResponse);
        }
        if(errorOccured){
            return operationResponseList;
        }else {
            operationResponseList.clear();
        }

        List<OperationResponse<InventoryPojo>> operationResponses =  inventoryFlow.batchAdd(inventoryPojoList);

        // converting list of ErrorResponse<InventoryPojo> to ErrorResponse<InventoryForm>
        for(OperationResponse<InventoryPojo> operationResponsePojo: operationResponses){
            OperationResponse<InventoryForm> operationResponse = new OperationResponse<>();
            InventoryForm inventoryForm = DtoHelper.convertInventoryPojoToInventoryForm(operationResponsePojo.getData());
            operationResponse.setData(inventoryForm);
            operationResponse.setMessage(operationResponsePojo.getMessage());
            operationResponseList.add(operationResponse);
        }
        return operationResponseList;
    }

    public List<InventoryData> getAll(){
        List<InventoryPojo> inventoryPojoList = inventoryApi.getAll();
        List<InventoryData> inventoryDataList = new ArrayList<>();
        for(InventoryPojo inventoryPojo: inventoryPojoList){
            inventoryDataList.add(DtoHelper.convertInventoryPojoToInventoryData(inventoryPojo));
        }
        return inventoryDataList;
    }

    public void edit(InventoryForm inventoryForm) throws ApiException {
        DtoHelper.validateInventoryForm(inventoryForm);
        InventoryPojo inventoryPojo = DtoHelper.convertInventoryFormToInventoryPojo(inventoryForm);
        inventoryFlow.edit(inventoryPojo);
    }
}
