package com.increff.pos.controller;

import com.increff.pos.dto.InventoryDto;
import com.increff.pos.model.data.InventoryData;
import com.increff.pos.model.form.InventoryForm;
import com.increff.pos.utils.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api
@RestController
@RequestMapping("/api/inventories")
public class InventoryController {
    @Autowired
    private InventoryDto inventoryDto;

    @ApiOperation("update inventory of a product")
    @RequestMapping(path = "/update", method = RequestMethod.PUT)
    public void update(@RequestBody InventoryForm inventoryForm) throws ApiException {
        inventoryDto.update(inventoryForm);
    }

    @ApiOperation("Update inventories of mulitple products")
    @RequestMapping(path = "/batch-update", method = RequestMethod.PUT)
    public void batchUpdate(@RequestBody List<InventoryForm> inventoryFormList) throws ApiException{
        inventoryDto.batchUpdate(inventoryFormList);
    }

    @ApiOperation("get all inventories")
    @RequestMapping(path = "/get-all", method = RequestMethod.GET)
    public List<InventoryData> getAll(){
        return inventoryDto.getAll();
    }
}
