package com.increff.pos.dto;

import com.increff.pos.api.ProductApi;
import com.increff.pos.flow.ProductFlow;
import com.increff.pos.model.data.ProductData;
import com.increff.pos.model.form.ProductForm;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.utils.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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

    public void batchAdd(List<ProductForm> productFormList) throws ApiException{
        StringBuilder failureMessage = new StringBuilder();
        int row = 1;
        List<ProductPojo> productPojoList = new ArrayList<>();
        for(ProductForm productForm: productFormList){
            try{
                DtoHelper.normalizeProductForm(productForm);
                DtoHelper.validateProductForm(productForm);
                ProductPojo productPojo = DtoHelper.convertProductFormToProductPojo(productForm);
                productPojoList.add(productPojo);
            } catch (ApiException e){
                failureMessage.append("row "+row+": "+e.getMessage()+".\n");
            }
            row++;
        }
        if(failureMessage.length()!=0){
            throw new ApiException(failureMessage.toString());
        }
        productApi.batchAdd(productPojoList);
    }



}
