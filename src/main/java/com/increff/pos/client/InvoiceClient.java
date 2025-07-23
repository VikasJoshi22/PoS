package com.increff.pos.client;

import com.increff.pos.model.data.InvoiceData;
import com.increff.pos.spring.ApplicationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.ZonedDateTime;
import java.util.List;

@Service
public class InvoiceClient {

    @Autowired
    private ApplicationProperties properties;
    @Autowired
    private RestTemplate restTemplate;

    private final String GENERATE_INVOICE = "/generate-invoice";
    private final String DOWNLOAD_INVOICE = "/download/";

    public String generateInvoice(InvoiceData invoiceData){
        String url = properties.getInvoiceBaseUrl()+ GENERATE_INVOICE;
        return restTemplate.postForObject(url, invoiceData, String.class);
    }

    public String downloadInvoice(Integer orderId){
        String url = properties.getInvoiceBaseUrl()+ DOWNLOAD_INVOICE +orderId;
        return restTemplate.getForObject(url, String.class);
    }

    public List<Integer> getInvoiceBetweenDates(ZonedDateTime startDate, ZonedDateTime endDate) {
        String url = properties.getInvoiceBaseUrl()+"/get-between-dates"+"?startDate="+startDate.toString()+"&endDate="+endDate.toString();
        ResponseEntity<List<Integer>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Integer>>() {}
        );
        return response.getBody();
    }
}
