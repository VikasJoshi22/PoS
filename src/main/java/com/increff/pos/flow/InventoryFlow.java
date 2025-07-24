package com.increff.pos.flow;

import com.increff.pos.api.InventoryApi;
import com.increff.pos.api.ProductApi;
import com.increff.pos.model.data.OperationResponse;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.utils.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.media.jai.operator.ErodeDescriptor;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@Transactional(rollbackFor = ApiException.class)
public class InventoryFlow {
    @Autowired
    private ProductApi productApi;

    public ProductPojo getProductByBarcode(String barcode) throws ApiException{
        ProductPojo productPojo = productApi.getByBarcode(barcode);
        if(Objects.isNull(productPojo)){
            throw new ApiException("Barcode doesn't exists.");
        }
        return productPojo;
    }

    public ProductPojo getProductByProductId(Integer productId) throws ApiException {
        return productApi.getById(productId);
    }

}
