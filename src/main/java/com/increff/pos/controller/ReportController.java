package com.increff.pos.controller;

import com.increff.pos.dto.ReportDto;
import com.increff.pos.model.data.DailySalesReportData;
import com.increff.pos.model.data.SalesReportData;
import com.increff.pos.model.form.DailySalesReportForm;
import com.increff.pos.model.form.SalesReportForm;
import com.increff.pos.utils.ApiException;
import com.increff.pos.utils.Constants;
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
    public List<DailySalesReportData> getDailySalesReport(@ModelAttribute DailySalesReportForm dailySalesReportForm){
        if(dailySalesReportForm.getStartDate().isEmpty()){
            dailySalesReportForm.setStartDate(Constants.MIN_DATE);
        }
        if(dailySalesReportForm.getEndDate().isEmpty()){
            dailySalesReportForm.setEndDate(ZonedDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        }
        return reportDto.getDailySalesReport(dailySalesReportForm);
    }


    @RequestMapping(path = "daily-sales-report/get-total-count", method = RequestMethod.GET)
    public Long getTotalCount(@RequestParam(defaultValue = "") String startDate, @RequestParam(defaultValue = "") String endDate){
        return reportDto.getTotalCount(startDate, endDate);
    }

    @ApiOperation("get sales report bases on date range, client, product")
    @RequestMapping(path = "/sales-report", method = RequestMethod.GET)
    public List<SalesReportData> getSalesReport(@ModelAttribute SalesReportForm salesReportForm) throws ApiException {
        return reportDto.getSalesReport(salesReportForm);
    }
}
