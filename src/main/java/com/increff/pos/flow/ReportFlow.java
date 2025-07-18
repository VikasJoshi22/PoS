package com.increff.pos.flow;

import com.increff.pos.api.*;
import com.increff.pos.model.data.SalesReportData;
import com.increff.pos.model.form.SalesReportForm;
import com.increff.pos.pojo.*;
import com.increff.pos.utils.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.*;

@Component
@Transactional
public class ReportFlow {
    @Autowired
    private OrderApi orderApi;
    @Autowired
    private OrderItemApi orderItemApi;
    @Autowired
    private DailySalesReportApi reportApi;
    @Autowired
    private ProductApi productApi;
    @Autowired
    private ClientApi clientApi;

    public void updateDailySalesReport(){
        // fetching orders of the day
        ZonedDateTime dateTime = ZonedDateTime.now();
        ZonedDateTime startDate = dateTime.minusDays(1).with(LocalTime.of(0,0,0));
        ZonedDateTime endDate = dateTime.minusDays(1).with(LocalTime.of(23,59,59));
        List<OrderPojo> orderPojoList =  orderApi.getBetweenDates(startDate, endDate);


        int invoicedOrderCount = 0;
        int invoicedItemsCount = 0;
        Double totalRevenue = 0.0;
        for(OrderPojo orderPojo: orderPojoList){
            //fetching orderItems
            List<OrderItemPojo> orderItemPojoList = orderItemApi.getByOrderId(orderPojo.getId());
            if(orderPojo.isInvoiced()){
                invoicedOrderCount++;
                invoicedItemsCount+=orderItemPojoList.size();
                for(OrderItemPojo orderItemPojo: orderItemPojoList){
                    totalRevenue += (orderItemPojo.getSellingPrice()*orderItemPojo.getQuantity());
                }
            }
        }
        DailySalesReportPojo dailySalesReportPojo = new DailySalesReportPojo();
        dailySalesReportPojo.setDateTime(startDate);
        dailySalesReportPojo.setTotalRevenue(totalRevenue);
        dailySalesReportPojo.setInvoicedOrdersCount(invoicedOrderCount);
        dailySalesReportPojo.setInvoicedItemsCount(invoicedItemsCount);
        reportApi.addDailySalesReport(dailySalesReportPojo);
    }

    public List<SalesReportData> getSalesReport(SalesReportForm salesReportForm) throws ApiException {
        // TODO: check which time we are getting with dates, error is possible based on that;
        List<OrderPojo> orderPojoList = new ArrayList<>();
        try{
         orderPojoList = orderApi.getBetweenDates(ZonedDateTime.parse(salesReportForm.getStartDate()), ZonedDateTime.parse(salesReportForm.getEndDate()));
        } catch (Exception e){
            throw new ApiException("error while getting orderPojoList");
        }



        // storing revenue and quantity of each product in map
        Map<Integer, Double> productRevenueMap = new HashMap<>();
        Map<Integer, Integer> productQuantityMap = new HashMap<>();
        for(OrderPojo orderPojo: orderPojoList){
            List<OrderItemPojo> orderItemPojoList = orderItemApi.getByOrderId(orderPojo.getId());
            for(OrderItemPojo orderItemPojo: orderItemPojoList){
                // inserting total sale value of the order item
                productRevenueMap.put(
                        orderItemPojo.getProductId(), productQuantityMap.getOrDefault(
                                orderItemPojo.getProductId(), 0
                        ) + (orderItemPojo.getQuantity()*orderItemPojo.getSellingPrice())
                );

                //inserting quantity of a product
                productQuantityMap.put(
                        orderItemPojo.getProductId(), productQuantityMap.getOrDefault(
                                orderItemPojo.getProductId(), 0
                        ) + (orderItemPojo.getQuantity())
                );
            }
        }

        List<SalesReportData> salesReportDataList = new ArrayList<>();
        for(Map.Entry<Integer, Integer> productQuantity: productQuantityMap.entrySet()){
            SalesReportData salesReportData = new SalesReportData();
            // fetching product
            ProductPojo productPojo = productApi.getById(productQuantity.getKey());
            // fetching client
            ClientPojo clientPojo = clientApi.getById(productPojo.getClientId());

            //setting values to salesReportData
            salesReportData.setProductBarcode(productPojo.getBarcode());
            salesReportData.setQuantity(productQuantity.getValue());
            salesReportData.setClient(clientPojo.getName());
            salesReportData.setRevenue(productRevenueMap.get(productQuantity.getKey()));

            salesReportDataList.add(salesReportData);
        }

        if(!salesReportForm.getProductBarcode().isEmpty()){

            // if both product and client is not null, then we will check if client has the product or not otherwise the list will be empty
            if(!salesReportForm.getClient().isEmpty()){
                ProductPojo productPojo = productApi.getByBarcode(salesReportForm.getProductBarcode());
                if(Objects.isNull(productPojo)){
                    throw new ApiException("product doesn't exists");
                }
                ClientPojo clientPojo = clientApi.getById(productPojo.getClientId());
                if(!clientPojo.getName().equals( salesReportForm.getClient() )){
                    throw new ApiException("Client '"+salesReportForm.getClient()+"' doesn't has Product with barcode '"+salesReportForm.getProductBarcode()+"'.");
                }
            }

            // if product is not null, then we will filter the list based on product. in this case, client doesn't matter
            List<SalesReportData> filteredSalesReportDataList = new ArrayList<>();
            for(SalesReportData salesReportData: salesReportDataList){
                if(salesReportData.getProductBarcode().equals(salesReportForm.getProductBarcode())){
                    filteredSalesReportDataList.add(salesReportData);
                }
            }
            return filteredSalesReportDataList;

        } else if(!salesReportForm.getClient().isEmpty()){
            List<SalesReportData> filteredSalesReportDataList = new ArrayList<>();
            for(SalesReportData salesReportData: salesReportDataList){
                if(salesReportData.getClient().equals(salesReportForm.getClient())){
                    filteredSalesReportDataList.add(salesReportData);
                }
            }
            return filteredSalesReportDataList;
        }

        // if control is here, then both product and client is null
        return salesReportDataList;
    }
}
