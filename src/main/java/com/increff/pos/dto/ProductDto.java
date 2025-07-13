package com.increff.pos.dto;

import com.increff.pos.api.ProductApi;
import com.increff.pos.flow.ProductFlow;
import com.increff.pos.model.data.ErrorResponse;
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

    public List<ErrorResponse<ProductForm>> batchAdd(List<ProductForm> productFormList) throws ApiException{
        List<ProductPojo> productPojoList = new ArrayList<>();
        List<ErrorResponse<ProductForm>> errorResponseList = new ArrayList<>();

        Set<String> barcodes = new HashSet<>();
        for(ProductForm productForm: productFormList){
            try{
                if(barcodes.contains(productForm.getBarcode())){
                    throw new ApiException("file contain duplicate barcodes");
                } else {
                    barcodes.add(productForm.getBarcode());
                }
                DtoHelper.normalizeProductForm(productForm);
                DtoHelper.validateProductForm(productForm);
                ProductPojo productPojo = DtoHelper.convertProductFormToProductPojo(productForm);
                productPojoList.add(productPojo);
            } catch (ApiException e){
                //pushing error to error list
                ErrorResponse<ProductForm> errorResponse = new ErrorResponse<>();
                errorResponse.setData(productForm);
                errorResponse.setMessage(e.getMessage()+"\n");
                errorResponseList.add(errorResponse);
            }
        }
        if(!errorResponseList.isEmpty()){
            return errorResponseList;
        }

        List<ErrorResponse<ProductPojo>> errorResponses =  productApi.batchAdd(productPojoList);

        // converting errorResponse of ProductPojo to ProductForm
        for(ErrorResponse<ProductPojo> errorResponse: errorResponses){
            ProductForm productForm = DtoHelper.convertProductPojoToProductForm(errorResponse.getData());
            ErrorResponse<ProductForm> errorProductForm = new ErrorResponse<>();
            errorProductForm.setMessage(errorResponse.getMessage());
            errorProductForm.setData(productForm);

            errorResponseList.add(errorProductForm);
        }

        return errorResponseList;
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
