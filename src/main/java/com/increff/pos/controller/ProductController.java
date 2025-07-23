package com.increff.pos.controller;

import com.increff.pos.dto.ProductDto;
import com.increff.pos.model.data.OperationResponse;
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
@RequestMapping("/api/products/supervisor")
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
    public List<ProductData> getAll(@RequestParam Integer page, @RequestParam Integer size, @RequestParam(defaultValue = "") String keyword) throws ApiException{
        return productDto.getAll(page, size, keyword);
    }

    @ApiOperation("Update a product's details")
    @RequestMapping(path = "/update/{id}", method = RequestMethod.PUT)
    public void update(@PathVariable Integer id, @RequestBody ProductForm productForm) throws ApiException{
        productDto.update(id, productForm);
    }

    @ApiOperation("Add multiple products using tsv.")
    @RequestMapping(path = "/batch-add", method = RequestMethod.POST)
    public List<OperationResponse<ProductForm>> batchAdd(@RequestBody List<ProductForm> productFormList) throws ApiException{
        return productDto.batchAdd(productFormList);
    }

    @ApiOperation("get by id")
    @RequestMapping(path = "/get/{id}", method = RequestMethod.GET)
    public ProductData getById(@PathVariable Integer id) throws ApiException{
        return productDto.getById(id);
    }

    @ApiOperation("Get by barcode")
    @RequestMapping(path = "/get-by-barcode/{barcode}", method = RequestMethod.GET)
    public ProductData getByBarcode(@PathVariable String barcode) throws ApiException{
        return productDto.getByBarcode(barcode);
    }

    @ApiOperation("getting total no. of products")
    @RequestMapping(path = "/get-total-count", method = RequestMethod.GET)
    public Long getTotalCount(){
        return productDto.getTotalCount();
    }

    @ApiOperation("search by barcode")
    @RequestMapping(path = "/search-by-barcode", method = RequestMethod.GET)
    public List<String> searchByBarcode(@RequestParam Integer page, @RequestParam Integer size, @RequestParam String barcode){
        return productDto.searchByBarcode(page, size, barcode);
    }

}
