package com.increff.pos.api;

import com.increff.pos.dao.DailySalesReportDao;
import com.increff.pos.model.form.DailySalesReportForm;
import com.increff.pos.pojo.DailySalesReportPojo;
import com.increff.pos.utils.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

@Service
@Transactional(rollbackFor = ApiException.class)
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

    public List<DailySalesReportPojo> getDailySalesReport(DailySalesReportForm filters){
        ZonedDateTime start = ZonedDateTime.parse(filters.getStartDate());
        ZonedDateTime end = ZonedDateTime.parse(filters.getEndDate());

        return reportDao.getDailySalesReport(start, end, filters.getPage(), filters.getSize());
    }

    public Long getTotalCount(ZonedDateTime startDate, ZonedDateTime endDate) {
        return reportDao.getTotalCount(startDate, endDate);
    }
}
