package com.increff.pos.controller;

import com.increff.pos.dto.ProductDto;
import com.increff.pos.model.data.ErrorResponse;
import com.increff.pos.model.data.ProductData;
import com.increff.pos.model.form.ProductForm;
import com.increff.pos.utils.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    private ProductDto productDto;


    @ApiOperation("add a single product")
    @RequestMapping(path = "/add", method = RequestMethod.POST)
    public void add(@RequestBody ProductForm productForm) throws ApiException {
        productDto.add(productForm);
    }

    @ApiOperation("get all client's info")
    @RequestMapping(path = "/get-all", method = RequestMethod.GET)
    public List<ProductData> getAll(){
        return productDto.getAll();
    }

    @ApiOperation("Update a product's details")
    @RequestMapping(path = "/update/{id}", method = RequestMethod.PUT)
    public void update(@PathVariable Integer id, @RequestBody ProductForm productForm) throws ApiException{
        productDto.update(id, productForm);
    }

    @ApiOperation("Add multiple products using tsv.")
    @RequestMapping(path = "/batch-add", method = RequestMethod.POST)
    public List<ErrorResponse<ProductForm>> batchAdd(@RequestBody List<ProductForm> productFormList) throws ApiException{
        return productDto.batchAdd(productFormList);
    }

    @ApiOperation("get by id")
    @RequestMapping(path = "/get/{id}", method = RequestMethod.GET)
    public ProductData getById(@PathVariable Integer id) throws ApiException{
        return productDto.getById(id);
    }

    @ApiOperation("Get by barcode")
    @RequestMapping(path = "/get-by-barcode/{barcode}")
    public ProductData getByBarcode(@PathVariable String barcode) throws ApiException{
        return productDto.getByBarcode(barcode);
    }

}
