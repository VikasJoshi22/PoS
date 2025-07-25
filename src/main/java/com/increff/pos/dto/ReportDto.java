package com.increff.pos.dto;

import com.increff.pos.api.DailySalesReportApi;
import com.increff.pos.flow.ReportFlow;
import com.increff.pos.model.data.DailySalesReportData;
import com.increff.pos.model.data.SalesReportData;
import com.increff.pos.model.form.DailySalesReportForm;
import com.increff.pos.model.form.SalesReportForm;
import com.increff.pos.pojo.DailySalesReportPojo;
import com.increff.pos.utils.ApiException;
import com.increff.pos.utils.UtilMethods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.List;

@Component
public class ReportDto {
    @Autowired
    private ReportFlow reportFlow;
    @Autowired
    private DailySalesReportApi reportApi;

    public void updateDailySalesReport(){
        reportFlow.updateDailySalesReport();
    }

    public List<DailySalesReportData> getDailySalesReport(DailySalesReportForm filters){
        List<DailySalesReportPojo> dailySalesReportPojoList = reportApi.getDailySalesReport(filters);
        return DtoHelper.convertDailySalesReportPojoListToDataList(dailySalesReportPojoList);
    }

    public List<SalesReportData> getSalesReport(SalesReportForm salesReportForm) throws ApiException {
        UtilMethods.normalizeSalesReportForm(salesReportForm);
        return reportFlow.getSalesReport(salesReportForm);
    }

    public Long getTotalCount(String startDate, String endDate) {
        ZonedDateTime start = UtilMethods.parseStartDate(startDate);;
        ZonedDateTime end = UtilMethods.parseEndDate(endDate);
        return reportApi.getTotalCount(start, end);
    }
}
