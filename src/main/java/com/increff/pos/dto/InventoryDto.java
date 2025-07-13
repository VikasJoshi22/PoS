package com.increff.pos.dto;

import com.increff.pos.api.InventoryApi;
import com.increff.pos.flow.InventoryFlow;
import com.increff.pos.model.data.ErrorResponse;
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

    public List<ErrorResponse<InventoryForm>> batchAdd(List<InventoryForm> inventoryFormList){
        List<InventoryPojo> inventoryPojoList = new ArrayList<>();
        List<ErrorResponse<InventoryForm>> errorResponseList = new ArrayList<>();

        for(InventoryForm inventoryForm: inventoryFormList){
            try{
                DtoHelper.validateInventoryForm(inventoryForm);
            }catch (ApiException e){
                ErrorResponse<InventoryForm> errorResponse = new ErrorResponse<>();
                errorResponse.setData(inventoryForm);
                errorResponse.setMessage(e.getMessage()+"\n");
                errorResponseList.add(errorResponse);
            }
            InventoryPojo inventoryPojo = DtoHelper.convertInventoryFormToInventoryPojo(inventoryForm);
            inventoryPojoList.add(inventoryPojo);
        }
        if(!errorResponseList.isEmpty()){
            return errorResponseList;
        }

        List<ErrorResponse<InventoryPojo>> errorResponses =  inventoryFlow.batchAdd(inventoryPojoList);

        // converting list of ErrorResponse<InventoryPojo> to ErrorResponse<InventoryForm>
        for(ErrorResponse<InventoryPojo> errorResponsePojo: errorResponses){
            ErrorResponse<InventoryForm> errorResponse = new ErrorResponse<>();
            InventoryForm inventoryForm = DtoHelper.convertInventoryPojoToInventoryForm(errorResponsePojo.getData());
            errorResponse.setData(inventoryForm);
            errorResponse.setMessage(errorResponsePojo.getMessage());
            errorResponseList.add(errorResponse);
        }
        return errorResponseList;
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
