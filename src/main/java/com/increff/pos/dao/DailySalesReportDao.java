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
    private static final String getDailySalesReportQuery = "select p from DailySalesReportPojo p where p.dateTime>=:startDate and p.dateTime<=:endDate order by p.id desc";
    private static final String getTotalCountQuery = "select count(p) from DailySalesReportPojo p where p.dateTime between :startDate and :endDate";

    @PersistenceContext
    private EntityManager em;

    public void addDailySalesReport(DailySalesReportPojo dailySalesReportPojo){
        em.persist(dailySalesReportPojo);
    }

    public List<DailySalesReportPojo> getDailySalesReport(ZonedDateTime startDate, ZonedDateTime endDate, Integer page, Integer size){
        Query query = em.createQuery(getDailySalesReportQuery);
        query.setFirstResult(page*size);
        query.setMaxResults(size);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        return query.getResultList();
    }


    public Long getTotalCount(ZonedDateTime startDate, ZonedDateTime endDate) {
        Query query = em.createQuery(getTotalCountQuery);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        return (Long) query.getSingleResult();
    }
}
