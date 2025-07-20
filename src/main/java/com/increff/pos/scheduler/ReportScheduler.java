package com.increff.pos.scheduler;

import com.increff.pos.dto.ReportDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ReportScheduler {
    @Autowired
    private ReportDto reportDto;

    @Scheduled(fixedDelay = 60000)
    public void updateDailySalesReport(){
        reportDto.updateDailySalesReport();
    }
}
