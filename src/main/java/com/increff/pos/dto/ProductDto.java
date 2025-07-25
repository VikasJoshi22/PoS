package com.increff.pos.dto;

import com.increff.pos.api.ProductApi;
import com.increff.pos.flow.ProductFlow;
import com.increff.pos.model.data.OperationResponse;
import com.increff.pos.model.data.ProductData;
import com.increff.pos.model.form.ProductForm;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.utils.ApiException;
import com.increff.pos.utils.UtilMethods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ProductDto {
    @Autowired
    private ProductApi productApi;
    @Autowired
    private ProductFlow productFlow;

    public void add(ProductForm productForm) throws ApiException{
        UtilMethods.normalizeProductForm(productForm);
        UtilMethods.validateProductForm(productForm);
        ProductPojo productPojo = convert(productForm);
        productFlow.add(productPojo);
    }

    public List<ProductData> getAll(Integer page, Integer size, String keyword) throws ApiException{
        List<ProductPojo> productPojoList = productApi.getAll(page, size, keyword);

        // converting ProductPojoList to ProductDataList
        List<ProductData> productDataList = new ArrayList<>();
        for(ProductPojo productPojo: productPojoList){
            ProductData productData = convert(productPojo);
            productDataList.add(productData);
        }
        return productDataList;
    }

    public void update(Integer id, ProductForm productForm) throws ApiException{
        UtilMethods.normalizeProductForm(productForm);
        UtilMethods.validateProductForm(productForm);
        ProductPojo productPojo = convert(productForm);
        productFlow.update(id, productPojo);
    }

    public List<OperationResponse<ProductForm>> batchAdd(List<ProductForm> productFormList){
        List<ProductPojo> productPojoList = new ArrayList<>();
        List<OperationResponse<ProductForm>> operationResponseList = new ArrayList<>();

        boolean errorOccured = false;
        for(ProductForm productForm: productFormList){
            OperationResponse<ProductForm> operationResponse = new OperationResponse<>();
            operationResponse.setData(productForm);
            operationResponse.setMessage("No error");
            try{
                UtilMethods.normalizeProductForm(productForm);
                UtilMethods.validateProductForm(productForm);
                ProductPojo productPojo = convert(productForm);
                productPojoList.add(productPojo);
            } catch (ApiException e){
                errorOccured = true;
                operationResponse.setMessage(e.getMessage());
            }

            operationResponseList.add(operationResponse);
        }
        if(errorOccured){
            return operationResponseList;
        }

        List<OperationResponse<ProductPojo>> operationResponses = productFlow.batchAdd(productPojoList);
        int i=0;
        for(OperationResponse<ProductPojo> operationResponse: operationResponses){
            if(!operationResponse.getMessage().equals("No error")){
                operationResponseList.get(i).setMessage(operationResponse.getMessage());
            }
            i++;
        }
        return operationResponseList;
    }

    public ProductData getById(Integer id) throws ApiException{
        ProductPojo productPojo = productApi.getById(id);
        return convert(productPojo);
    }

    public ProductData getByBarcode(String barcode) throws ApiException{
        ProductPojo productPojo = productApi.getByBarcode(barcode);
        if(Objects.isNull(productPojo)){
            throw new ApiException("Product doesn't exists with barcode '"+barcode+"'. ");
        }
        return convert(productPojo);
    }

    public Long getTotalCount() {
        return productApi.getTotalCount();
    }

    public List<String> searchByBarcode(Integer page, Integer size, String barcode) {
        return productApi.searchByBarcode(page, size, barcode);
    }


    private ProductData convert(ProductPojo productPojo) throws ApiException{
        String clientName = productFlow.getClientById(productPojo.getClientId()).getName();
        Integer inventory = productFlow.getInventoryByProductId(productPojo.getId()).getQuantity();
        return DtoHelper.convertProductPojoToProductData(productPojo, clientName, inventory);
    }

    private ProductPojo convert(ProductForm productForm) throws ApiException{
        Integer clientId = productFlow.getClientByName(productForm.getClientName()).getId();
        return DtoHelper.convertProductFormToProductPojo(productForm, clientId);
    }

}
