package com.increff.pos.dto;

import com.increff.pos.api.DailySalesReportApi;
import com.increff.pos.flow.ReportFlow;
import com.increff.pos.model.data.DailySalesReportData;
import com.increff.pos.model.data.SalesReportData;
import com.increff.pos.model.form.SalesReportForm;
import com.increff.pos.pojo.DailySalesReportPojo;
import com.increff.pos.utils.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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

    public List<DailySalesReportData> getDailySalesReport(String startDate, String endDate){
        // converting Pojo to Data
        List<DailySalesReportPojo> dailySalesReportPojoList = reportApi.getDailySalesReport(startDate, endDate);
        List<DailySalesReportData> dailySalesReportDataList = new ArrayList<>();
        for(DailySalesReportPojo dailySalesReportPojo: dailySalesReportPojoList){
            DailySalesReportData dailySalesReportData = DtoHelper.convertDailySalesReportPojoToData(dailySalesReportPojo);
            dailySalesReportDataList.add(dailySalesReportData);
        }
        return dailySalesReportDataList;
    }

    public List<SalesReportData> getSalesReport(SalesReportForm salesReportForm) throws ApiException {
        //normalizing salesReportForm
        salesReportForm.setClient(salesReportForm.getClient().toLowerCase());
        salesReportForm.setProductBarcode(salesReportForm.getProductBarcode().toLowerCase());
        return reportFlow.getSalesReport(salesReportForm);
    }
}
