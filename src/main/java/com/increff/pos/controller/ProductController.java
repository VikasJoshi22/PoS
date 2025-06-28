package com.increff.pos.controller;

import com.increff.pos.dto.ProductDto;
import com.increff.pos.model.form.ProductForm;
import com.increff.pos.utils.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api
@RestController
@RequestMapping("/api/product")
public class ProductController {
    @Autowired
    private ProductDto productDto;


    @ApiOperation("add a single product")
    @RequestMapping("/add")
    public void add(@RequestBody ProductForm productForm) throws ApiException {
        productDto.add(productForm);
    }
}
