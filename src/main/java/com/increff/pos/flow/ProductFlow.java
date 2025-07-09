package com.increff.pos.flow;

import com.increff.pos.api.ClientApi;
import com.increff.pos.api.ProductApi;
import com.increff.pos.pojo.ClientPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.utils.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

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

    private void doesClientExists(Integer clientId) throws ApiException{
        try{
            ClientPojo clientPojo = clientApi.getById(clientId);
        }catch (ApiException e){
            throw new ApiException("Client doesn't exists");
        }
    }
}
