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

    // expects dateString in ISO format UTC timezone
    @ApiOperation("generating sales report")
    @RequestMapping(path = "/daily-sales-report", method = RequestMethod.GET)
    public List<DailySalesReportData> getDailySalesReport(
            @RequestParam String startDate,
            @RequestParam String endDate
    ){
        if(startDate.isEmpty()){
            startDate = "2025-07-15T10:47:38.803Z";
        }
        if(endDate.isEmpty()){
            endDate = ZonedDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
        }
        return reportDto.getDailySalesReport(startDate, endDate);
    }

    @ApiOperation("get sales report bases on date range, client, product")
    @RequestMapping(path = "/sales-report", method = RequestMethod.GET)
    public List<SalesReportData> getSalesReport(@ModelAttribute SalesReportForm salesReportForm) throws ApiException {
        return reportDto.getSalesReport(salesReportForm);
    }
}
