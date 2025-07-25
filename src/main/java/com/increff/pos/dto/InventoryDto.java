package com.increff.pos.dto;

import com.increff.pos.api.InventoryApi;
import com.increff.pos.flow.InventoryFlow;
import com.increff.pos.model.data.OperationResponse;
import com.increff.pos.model.data.InventoryData;
import com.increff.pos.model.form.InventoryForm;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.utils.ApiException;
import com.increff.pos.utils.UtilMethods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class InventoryDto {
    @Autowired
    private InventoryApi inventoryApi;

    @Autowired
    private InventoryFlow inventoryFlow;

    public void add(InventoryForm inventoryForm) throws ApiException {
        UtilMethods.normalizeInventoryForm(inventoryForm);
        UtilMethods.validateInventoryForm(inventoryForm);
        InventoryPojo inventoryPojo = convert(inventoryForm);
        inventoryApi.add(inventoryPojo);
    }

    // todo: can use add method for batchAdd,
    // correction: if some error occured, then we can't rollback since we can't use transactional in DTO
    public List<OperationResponse<InventoryForm>> batchAdd(List<InventoryForm> inventoryFormList){
        List<InventoryPojo> inventoryPojoList = new ArrayList<>();
        List<OperationResponse<InventoryForm>> operationResponseList = new ArrayList<>();

        boolean errorOccured = false;
        for(InventoryForm inventoryForm: inventoryFormList){
            OperationResponse<InventoryForm> operationResponse = new OperationResponse<>();
            operationResponse.setData(inventoryForm);
            operationResponse.setMessage("No error");
            try{
                UtilMethods.normalizeInventoryForm(inventoryForm);
                UtilMethods.validateInventoryForm(inventoryForm);
                InventoryPojo inventoryPojo = convert(inventoryForm);
                inventoryPojoList.add(inventoryPojo);
            }catch (ApiException e){
                operationResponse.setMessage(e.getMessage());
                errorOccured = true;
            }
            operationResponseList.add(operationResponse);
        }
        if(!errorOccured){
            inventoryApi.batchAdd(inventoryPojoList);
        }
        return operationResponseList;
    }

    public List<InventoryData> getAll() throws ApiException{
        List<InventoryPojo> inventoryPojoList = inventoryApi.getAll();
        List<InventoryData> inventoryDataList = new ArrayList<>();
        for(InventoryPojo inventoryPojo: inventoryPojoList){
            inventoryDataList.add(convert(inventoryPojo));
        }
        return inventoryDataList;
    }

    public void edit(InventoryForm inventoryForm) throws ApiException {
        UtilMethods.normalizeInventoryForm(inventoryForm);
        UtilMethods.validateInventoryForm(inventoryForm);
        InventoryPojo inventoryPojo = convert(inventoryForm);
        inventoryApi.edit(inventoryPojo);
    }

    public InventoryData getByProductId(Integer productId) throws ApiException{
        InventoryPojo inventoryPojo = inventoryApi.getByProductId(productId);
        if(Objects.isNull(inventoryPojo)){
            throw new ApiException("Out of stock");
        }
        return convert(inventoryPojo);
    }

    public InventoryData getByBarcode(String barcode) throws ApiException {
        Integer productId = inventoryFlow.getProductByBarcode(barcode).getId();
        return getByProductId(productId);
    }


    private InventoryPojo convert(InventoryForm inventoryForm) throws ApiException{
        Integer productId = inventoryFlow.getProductByBarcode(inventoryForm.getBarcode()).getId();
        return DtoHelper.convertInventoryFormToInventoryPojo(inventoryForm, productId);
    }

    private InventoryData convert(InventoryPojo inventoryPojo) throws ApiException{
        String barcode = inventoryFlow.getProductByProductId(inventoryPojo.getProductId()).getBarcode();
        return DtoHelper.convertInventoryPojoToInventoryData(inventoryPojo, barcode);
    }

}
