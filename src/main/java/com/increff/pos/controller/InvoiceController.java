package com.increff.pos.controller;

import com.increff.pos.dto.InvoiceDto;
import com.increff.pos.utils.ApiException;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@Api
@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {
    @Autowired
    private InvoiceDto invoiceDto;

    @RequestMapping(path = "generate-invoice/{orderId}", method = RequestMethod.PUT)
    public void getInvoiceData(@PathVariable Integer orderId, HttpServletResponse response) throws ApiException {
        invoiceDto.generateInvoice(orderId, response);
    }

    @RequestMapping(path = "download/{orderId}", method = RequestMethod.GET)
    public void downloadInvoice(@PathVariable Integer orderId, HttpServletResponse response) throws ApiException {
        invoiceDto.downloadInvoice(orderId, response);
    }
}
