package com.increff.pos.dto;

import com.increff.pos.model.data.ClientData;
import com.increff.pos.model.form.ClientForm;
import com.increff.pos.model.form.ProductForm;
import com.increff.pos.pojo.ClientPojo;
import com.increff.pos.pojo.ProductPojo;

public class DtoHelper {

    public static ClientPojo convertClientFormToClientPojo(ClientForm client){
        ClientPojo c = new ClientPojo();
        c.setName(client.getName().toLowerCase());
        return c;
    }

    public static ClientData convertClientPojoToClientData(ClientPojo clientPojo){
        ClientData clientData = new ClientData();
        clientData.setId(clientPojo.getId());
        clientData.setName(clientPojo.getName());
        return clientData;
    }

    public static ProductPojo convertProductFormToProductPojo(ProductForm productForm, ClientPojo clientPojo) {
        ProductPojo productPojo = new ProductPojo();
        productPojo.setBarcode(productForm.getBarcode());
        productPojo.setMrp(productForm.getMrp());
        productPojo.setImageUrl(productForm.getImageUrl());
        productPojo.setName(productForm.getName());
        productPojo.setClient(clientPojo);
        return productPojo;
    }
}
