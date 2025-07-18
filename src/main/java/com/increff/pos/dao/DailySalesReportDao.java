package com.increff.pos.dao;

import com.increff.pos.pojo.DailySalesReportPojo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.time.ZonedDateTime;
import java.util.List;

@Transactional
@Repository
public class DailySalesReportDao {
    private static final String getDailySalesReportQuery = "select p from DailySalesReportPojo p where p.dateTime between :startDate and :endDate";

    @PersistenceContext
    private EntityManager em;

    public void addDailySalesReport(DailySalesReportPojo dailySalesReportPojo){
        em.persist(dailySalesReportPojo);
    }

    public List<DailySalesReportPojo> getDailySalesReport(ZonedDateTime startDate, ZonedDateTime endDate){
        Query query = em.createQuery(getDailySalesReportQuery);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        return query.getResultList();
    }
}
