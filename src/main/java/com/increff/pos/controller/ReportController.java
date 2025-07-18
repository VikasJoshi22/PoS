package com.increff.pos.controller;

import com.increff.pos.dto.ReportDto;
import com.increff.pos.model.data.DailySalesReportData;
import com.increff.pos.model.data.SalesReportData;
import com.increff.pos.model.form.DailySalesReportForm;
import com.increff.pos.model.form.SalesReportForm;
import com.increff.pos.utils.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Api
@RestController
@RequestMapping("/api/reports")
public class ReportController {
    @Autowired
    private ReportDto reportDto;

    @ApiOperation("generating sales report")
    @RequestMapping(path = "/daily-sales-report", method = RequestMethod.GET)
    public List<DailySalesReportData> getDailySalesReport(
            @RequestParam String startDate,
            @RequestParam String endDate){

        DailySalesReportForm dailySalesReportForm = new DailySalesReportForm();

        if(startDate.isEmpty()){
            startDate = "2025-07-15T10:47:38.803Z";
        }
        dailySalesReportForm.setStartDate(startDate);

        if(endDate.isEmpty()){
            endDate = ZonedDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
        }
        dailySalesReportForm.setEndDate(endDate);

        return reportDto.getDailySalesReport(startDate, endDate);
    }

    @ApiOperation("get sales report bases on date range, client, product")
    @RequestMapping(path = "/sales-report", method = RequestMethod.GET)
    public List<SalesReportData> getSalesReport(
            @RequestParam String startDate,
            @RequestParam String endDate,
            @RequestParam String client,
            @RequestParam String productBarcode) throws ApiException {

        SalesReportForm salesReportForm = new SalesReportForm();
        //if startDate or endDate is empty, putting a default value
        if(startDate.isEmpty()){
            startDate = "2025-07-15T10:47:38.803Z";
        }
        salesReportForm.setStartDate(startDate);
        if(endDate.isEmpty()){
            endDate = ZonedDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
        }
        salesReportForm.setEndDate(endDate);

        salesReportForm.setClient(client);
        salesReportForm.setProductBarcode(productBarcode);


        return reportDto.getSalesReport(salesReportForm);
    }
}
