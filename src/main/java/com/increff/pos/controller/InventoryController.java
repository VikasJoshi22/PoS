package com.increff.pos.controller;

import com.increff.pos.dto.InventoryDto;
import com.increff.pos.model.data.ErrorResponse;
import com.increff.pos.model.data.InventoryData;
import com.increff.pos.model.form.InventoryForm;
import com.increff.pos.utils.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
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

    @ApiOperation("add inventory of a product")
    @RequestMapping(path = "/add", method = RequestMethod.PUT)
    public void add(@RequestBody InventoryForm inventoryForm) throws ApiException {
        inventoryDto.add(inventoryForm);
    }

    @ApiOperation("add inventories of mulitple products")
    @RequestMapping(path = "/batch-add", method = RequestMethod.PUT)
    public List<ErrorResponse<InventoryForm>> batchAdd(@RequestBody List<InventoryForm> inventoryFormList) throws ApiException{
        return inventoryDto.batchAdd(inventoryFormList);
    }

    @ApiOperation("edit inventory of a product")
    @RequestMapping(path = "/edit", method = RequestMethod.PUT)
    public void edit(@RequestBody InventoryForm inventoryForm) throws ApiException{
        inventoryDto.edit(inventoryForm);
    }

    @ApiOperation("get all inventories")
    @RequestMapping(path = "/get-all", method = RequestMethod.GET)
    public List<InventoryData> getAll(){
        return inventoryDto.getAll();
    }
}
