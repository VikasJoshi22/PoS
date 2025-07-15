package com.increff.pos.dto;

import com.google.common.io.ByteStreams;
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
        File document = invoiceFlow.generateInvoice(orderId);
        try {
            InputStream is = new FileInputStream(document);
            ByteStreams.copy(is, response.getOutputStream());
            response.setContentType("application/pdf");
            response.flushBuffer();
        } catch (Exception e) {
            throw new ApiException("Error while invoice generation");
        }
    }

    public void downloadInvoice(Integer orderId, HttpServletResponse response) throws ApiException{
        String url = "http://localhost:8000/invoice_app/api/invoices/download/"+orderId;
        String base64Pdf = restTemplate.getForObject(url, String.class);
        byte[] pdfBytes = Base64.getDecoder().decode(base64Pdf);
        File pdfFile = new File("src/main/resources/invoices/invoice" + orderId + ".pdf");
        try (FileOutputStream fos = new FileOutputStream(pdfFile)) {
            fos.write(pdfBytes);
        } catch (IOException e) {
            throw new ApiException("error while downloading pdf file.");
        }

        try {
            InputStream is = new FileInputStream(pdfFile);
            ByteStreams.copy(is, response.getOutputStream());
            response.setContentType("application/pdf");
            response.flushBuffer();
        } catch (Exception e) {
            throw new ApiException("Error while downloading invoice");
        }
    }
}
