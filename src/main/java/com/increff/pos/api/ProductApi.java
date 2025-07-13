package com.increff.pos.api;

import com.increff.pos.dao.ProductDao;
import com.increff.pos.model.data.ErrorResponse;
import com.increff.pos.model.form.ProductForm;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.utils.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Transactional(rollbackFor = ApiException.class)
public class ProductApi {
    @Autowired
    private ProductDao productDao;

    public void add(ProductPojo productPojo) throws ApiException{
        checkDuplicateBarcode(productPojo.getBarcode());
        productDao.add(productPojo);
    }

    public List<ProductPojo> getAll() {
        return productDao.getAll();
    }

    public void update(Integer id, ProductPojo productPojo) throws ApiException {

        //checking if product with given id exists or not, if exists then checking for duplicate barcode, only if it is getting updated.
        ProductPojo existingProduct = getById(id);
        if(Objects.isNull(existingProduct)){
            throw new ApiException("Product with id '"+id+"' doesn't exists");
        } else if (!existingProduct.getBarcode().equals(productPojo.getBarcode())) {
            checkDuplicateBarcode(productPojo.getBarcode());
        }
        productDao.update(id, productPojo);
    }

    public List<ErrorResponse<ProductPojo>> batchAdd(List<ProductPojo> productPojoList){
        List<ErrorResponse<ProductPojo>> errorResponseList = new ArrayList<>();
        for(ProductPojo productPojo: productPojoList){
            try{
                checkDuplicateBarcode(productPojo.getBarcode());
                productDao.add(productPojo);
            }catch (ApiException e){
                ErrorResponse<ProductPojo> errorResponse = new ErrorResponse<>();
                errorResponse.setData(productPojo);
                errorResponse.setMessage(e.getMessage()+"\n");
                errorResponseList.add(errorResponse);
            }
        }
        return errorResponseList;
    }

    public ProductPojo getByBarcode(String barcode){
        return productDao.getByBarcode(barcode);
    }

    public ProductPojo getById(Integer id) throws ApiException{
        return productDao.getById(id);
    }

    private void checkDuplicateBarcode(String barcode) throws ApiException{
        ProductPojo productPojo = productDao.getByBarcode(barcode);
        if(Objects.nonNull(productPojo)){
            throw new ApiException("Barcode should be unique. Product '"+productPojo.getName()+"' already has barcode: '"+ barcode+"'");
        }
    }

}
