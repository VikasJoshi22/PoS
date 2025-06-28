package com.increff.pos.dto;

import com.increff.pos.dao.ClientDao;
import com.increff.pos.dao.ProductDao;
import com.increff.pos.model.form.ProductForm;
import com.increff.pos.pojo.ClientPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.utils.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductDto {
    @Autowired
    private ClientDao clientDao;

    @Autowired
    private ProductDao productDao;

    public void add(ProductForm productForm) throws ApiException{
//        checkBarcode(productForm.getBarcode());
        ClientPojo clientPojo = null;

        clientPojo = clientDao.getByName(productForm.getClient());
        if(clientPojo==null){
            throw new ApiException("Client "+ productForm.getClient() +" doesn't exists");
        }

        ProductPojo productPojo = DtoHelper.convertProductFormToProductPojo(productForm, clientPojo);
        productDao.add(productPojo);

    }

    private void checkBarcode(String Barcode){
        //TODO
        //will implement later, after implementing getById api
    }
}
