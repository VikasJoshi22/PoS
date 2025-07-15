package com.increff.pos.dto;

import com.increff.pos.api.ProductApi;
import com.increff.pos.flow.ProductFlow;
import com.increff.pos.model.data.OperationResponse;
import com.increff.pos.model.data.ProductData;
import com.increff.pos.model.form.ProductForm;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.utils.ApiException;
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
        DtoHelper.normalizeProductForm(productForm);
        DtoHelper.validateProductForm(productForm);
        ProductPojo productPojo = DtoHelper.convertProductFormToProductPojo(productForm);
        productFlow.add(productPojo);
    }

    public List<ProductData> getAll(){
        List<ProductData> productDataList = new ArrayList<>();
        List<ProductPojo> productPojoList = productApi.getAll();

        // converting ProductPojoList to ProductDataList
        for(ProductPojo productPojo: productPojoList){
            ProductData productData = DtoHelper.convertProductPojoToProductData(productPojo);
            productDataList.add(productData);
        }

        return productDataList;
    }

    public void update(Integer id, ProductForm productForm) throws ApiException{
        DtoHelper.normalizeProductForm(productForm);
        DtoHelper.validateProductForm(productForm);
        ProductPojo productPojo = DtoHelper.convertProductFormToProductPojo(productForm);
        productFlow.update(id, productPojo);
    }

    public List<OperationResponse<ProductForm>> batchAdd(List<ProductForm> productFormList) throws ApiException{
        List<ProductPojo> productPojoList = new ArrayList<>();
        List<OperationResponse<ProductForm>> operationResponseList = new ArrayList<>();

        Set<String> barcodes = new HashSet<>();
        boolean errorOccured = false;
        for(ProductForm productForm: productFormList){
            OperationResponse<ProductForm> operationResponse = new OperationResponse<>();
            operationResponse.setData(productForm);
            operationResponse.setMessage("No error");
            try{
                DtoHelper.normalizeProductForm(productForm);
                DtoHelper.validateProductForm(productForm);
            } catch (ApiException e){
                //pushing error to error list
                errorOccured = true;
                operationResponse.setMessage(e.getMessage());
            }
            ProductPojo productPojo = DtoHelper.convertProductFormToProductPojo(productForm);
            productPojoList.add(productPojo);
            operationResponseList.add(operationResponse);
        }

        if(errorOccured){
            return operationResponseList;
        }else {
            operationResponseList.clear();
        }
        List<OperationResponse<ProductPojo>> operationResponses = productFlow.batchAdd(productPojoList);

        // converting errorResponse of ProductPojo to ProductForm
        for(OperationResponse<ProductPojo> operationResponse: operationResponses){
            ProductForm productForm = DtoHelper.convertProductPojoToProductForm(operationResponse.getData());
            OperationResponse<ProductForm> operationProductForm = new OperationResponse<>();
            operationProductForm.setMessage(operationResponse.getMessage());
            operationProductForm.setData(productForm);
            operationResponseList.add(operationProductForm);
        }
        return operationResponseList;
    }

    public ProductData getById(Integer id) throws ApiException{
        ProductPojo productPojo = productApi.getById(id);
        return DtoHelper.convertProductPojoToProductData(productPojo);
    }

    public ProductData getByBarcode(String barcode) throws ApiException{
        ProductPojo productPojo = productApi.getByBarcode(barcode);
        if(Objects.isNull(productPojo)){
            throw new ApiException("Product doesn't exists with barcode '"+barcode+"'. ");
        }
        return DtoHelper.convertProductPojoToProductData(productPojo);
    }

}
