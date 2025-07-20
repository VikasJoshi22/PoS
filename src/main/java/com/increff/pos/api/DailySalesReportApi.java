package com.increff.pos.api;

import com.increff.pos.dao.DailySalesReportDao;
import com.increff.pos.pojo.DailySalesReportPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

@Service
public class DailySalesReportApi {
    @Autowired
    private OrderApi orderApi;
    @Autowired
    private OrderItemApi orderItemApi;
    @Autowired
    private DailySalesReportDao reportDao;

    public void addDailySalesReport(DailySalesReportPojo dailySalesReportPojo){
        reportDao.addDailySalesReport(dailySalesReportPojo);
    }

    public List<DailySalesReportPojo> getDailySalesReport(String startDate, String endDate){
        ZonedDateTime start = ZonedDateTime.parse(startDate);
        ZonedDateTime end = ZonedDateTime.parse(endDate);

        return reportDao.getDailySalesReport(ZonedDateTime.parse(startDate), ZonedDateTime.parse(endDate));
    }
}
