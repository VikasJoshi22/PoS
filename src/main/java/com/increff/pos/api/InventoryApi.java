package com.increff.pos.api;

import com.increff.pos.dao.InventoryDao;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.utils.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class InventoryApi {
    @Autowired
    private InventoryDao inventoryDao;

    public void update(InventoryPojo inventoryPojo){
        // checking if inventory already exists
        InventoryPojo inventory = inventoryDao.getByProduct(inventoryPojo.getProductId());
        //if it doesn't exist then we will create a new row, otherwise update the existing inventory
        if(Objects.isNull(inventory)){
            inventoryDao.add(inventoryPojo);
        }else{
            inventoryDao.update(inventoryPojo);
        }
    }

    public void batchUpdate(List<InventoryPojo> inventoryPojoList) {
        for(InventoryPojo inventoryPojo: inventoryPojoList){
            // checking if inventory already exists
            InventoryPojo inventory = inventoryDao.getByProduct(inventoryPojo.getProductId());
            //if it doesn't exist then we will create a new row, otherwise update the existing inventory
            if(Objects.isNull(inventory)){
                inventoryDao.add(inventoryPojo);
            }else{
                inventoryDao.update(inventoryPojo);
            }
        }
    }

    public List<InventoryPojo> getAll() {
        return inventoryDao.getAll();
    }

    public InventoryPojo getByProductId(Integer productId){
        return inventoryDao.getByProduct(productId);
    }
}
