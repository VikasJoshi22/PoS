package com.increff.pos.dto;

import com.increff.pos.flow.InvoiceFlow;
import com.increff.pos.utils.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Base64;

@Component
public class InvoiceDto {
    @Autowired
    private InvoiceFlow invoiceFlow;
    private final RestTemplate restTemplate = new RestTemplate();

    public void generateInvoice(Integer orderId, HttpServletResponse response) throws ApiException {
        String base64Pdf = invoiceFlow.generateInvoice(orderId);
        try {
            byte[] pdfBytes = Base64.getDecoder().decode(base64Pdf);
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=invoice-"+orderId+".pdf"); // or inline;
            response.setContentLength(pdfBytes.length);
            OutputStream out = response.getOutputStream();
            out.write(pdfBytes);
            out.flush();
        } catch (Exception e) {
            throw new ApiException("Error while invoice generation");
        }
    }

    public void downloadInvoice(Integer orderId, HttpServletResponse response) throws ApiException{
        String url = "http://localhost:8000/invoice_app/api/invoices/download/"+orderId;
        String base64Pdf = restTemplate.getForObject(url, String.class);
        byte[] pdfBytes = Base64.getDecoder().decode(base64Pdf);
        try {
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=myfile.pdf"); // or inline;
            response.setContentLength(pdfBytes.length);
            OutputStream out = response.getOutputStream();
            out.write(pdfBytes);
            out.flush();
        } catch (Exception e) {
            throw new ApiException("error while downloading invoice.");
        }
    }
}
