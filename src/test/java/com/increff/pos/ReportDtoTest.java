package com.increff.pos;

import com.increff.pos.dao.DailySalesReportDao;
import com.increff.pos.dto.ReportDto;
import com.increff.pos.model.data.DailySalesReportData;
import com.increff.pos.model.data.SalesReportData;
import com.increff.pos.model.form.DailySalesReportForm;
import com.increff.pos.model.form.SalesReportForm;
import com.increff.pos.pojo.DailySalesReportPojo;
import com.increff.pos.utils.ApiException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.Assert.*;

public class ReportDtoTest extends AbstractUnitTest {

    @Autowired
    private ReportDto reportDto;

    @Autowired
    private DailySalesReportDao dailySalesReportDao;

    @Test
    public void testGetDailySalesReport() {
        ZonedDateTime dateTime = ZonedDateTime.now();
        DailySalesReportPojo pojo = TestHelper.createDailySalesReportPojo(dateTime, 5, 10, 1000.0);
        dailySalesReportDao.addDailySalesReport(pojo);

        DailySalesReportForm form = TestHelper.createDailySalesReportForm(
            dateTime.minusDays(1).toString(), 
            dateTime.plusDays(1).toString(), 
            0, 
            10
        );
        List<DailySalesReportData> result = reportDto.getDailySalesReport(form);
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(5, result.get(0).getInvoicedOrdersCount().intValue());
    }

    @Test
    public void testGetDailySalesReportEmpty() {
        DailySalesReportForm form = TestHelper.createDailySalesReportForm(
            ZonedDateTime.now().minusDays(10).toString(), 
            ZonedDateTime.now().minusDays(5).toString(), 
            0, 
            10
        );
        List<DailySalesReportData> result = reportDto.getDailySalesReport(form);
        
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testGetSalesReportWithEmptyFilters() throws ApiException {
        SalesReportForm form = TestHelper.createSalesReportForm(
            ZonedDateTime.now().minusDays(1).toString(),
            ZonedDateTime.now().toString(),
            "",
            ""
        );
        List<SalesReportData> result = reportDto.getSalesReport(form);
        
        assertNotNull(result);
    }

    @Test
    public void testGetTotalCount() {
        ZonedDateTime dateTime = ZonedDateTime.now();
        DailySalesReportPojo pojo = TestHelper.createDailySalesReportPojo(dateTime, 3, 6, 500.0);
        dailySalesReportDao.addDailySalesReport(pojo);

        Long result = reportDto.getTotalCount(
            dateTime.minusDays(1).toString(), 
            dateTime.plusDays(1).toString()
        );
        
        assertNotNull(result);
        assertEquals(1L, result.longValue());
    }

    @Test
    public void testGetTotalCountEmpty() {
        Long result = reportDto.getTotalCount("", "");
        
        assertNotNull(result);
        assertEquals(0L, result.longValue());
    }
} 