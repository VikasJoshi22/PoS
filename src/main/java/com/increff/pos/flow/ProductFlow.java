package com.increff.pos.flow;

import com.increff.pos.api.ClientApi;
import com.increff.pos.api.ProductApi;
import com.increff.pos.model.data.OperationResponse;
import com.increff.pos.pojo.ClientPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.utils.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ProductFlow {
    @Autowired
    private ClientApi clientApi;
    @Autowired
    private ProductApi productApi;

    public void add(ProductPojo productPojo) throws ApiException {
        doesClientExists(productPojo.getClientId());
        productApi.add(productPojo);
    }

    public void update(Integer id, ProductPojo productPojo) throws ApiException{
        doesClientExists(productPojo.getClientId());
        productApi.update(id, productPojo);
    }

    public List<OperationResponse<ProductPojo>> batchAdd(List<ProductPojo> productPojoList) {
        List<OperationResponse<ProductPojo>> operationResponseList = new ArrayList<>();

        Set<String> barcodes = new HashSet<>();
        boolean errorOccured = false;
        for(ProductPojo productPojo: productPojoList){
            OperationResponse<ProductPojo> operationResponse = new OperationResponse<>();
            operationResponse.setData(productPojo);
            operationResponse.setMessage("No error");
            try{
                if(barcodes.contains(productPojo.getBarcode())){
                    throw new ApiException("file contain duplicate barcodes");
                } else {
                    barcodes.add(productPojo.getBarcode());
                }
                doesClientExists(productPojo.getClientId());
            } catch (ApiException e){
                //pushing error to error list
                errorOccured = true;
                operationResponse.setMessage(e.getMessage());
            }
            operationResponseList.add(operationResponse);
        }

        if(errorOccured){
            return operationResponseList;
        }else {
            operationResponseList.clear();
        }
        return productApi.batchAdd(productPojoList);
    }

    private void doesClientExists(Integer clientId) throws ApiException{
        try{
            ClientPojo clientPojo = clientApi.getById(clientId);
        }catch (ApiException e){
            throw new ApiException("Client doesn't exists");
        }
    }

}
