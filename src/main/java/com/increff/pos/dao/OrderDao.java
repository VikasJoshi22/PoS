package com.increff.pos.dao;

import com.increff.pos.model.form.OrderFilters;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.utils.ApiException;
import com.increff.pos.utils.Constants;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

@Repository
@Transactional(rollbackFor = ApiException.class)
public class OrderDao {
    private static final String getAllOrdersQuery = "select p from OrderPojo p";
    private static final String getByIdQuery = "select p from OrderPojo p where id=:id";
    private static final String updateQuery = "update OrderPojo p set p.dateTime=:dateTime, p.status=:status where id=:id";
    private static final String getBetweenDatesQuery = "select p from OrderPojo p where p.dateTime between :startDate and :endDate";
    private static final String getTotalCountQuery = "select count(p) from OrderPojo p where p.dateTime between :startDate and :endDate";

    @PersistenceContext
    private EntityManager em;

    public Integer addOrder(OrderPojo orderPojo){
        em.persist(orderPojo);
        return orderPojo.getId();
    }

    public void update(Integer id, OrderPojo orderPojo){
        Query query = em.createQuery(updateQuery);
        query.setParameter("dateTime", orderPojo.getDateTime());
        query.setParameter("status", orderPojo.getStatus());
        query.setParameter("id", id);

        query.executeUpdate();
    }


    public List<OrderPojo> getAllOrders(OrderFilters orderFilters) {
        String editedQuery = new String(getBetweenDatesQuery);
        if(!Objects.isNull(orderFilters.getOrderId())){
            editedQuery+=" and p.id=:orderId";
        }
        if(!orderFilters.getStatus().isEmpty()){
            editedQuery+=" and p.status=:status";
        }
        editedQuery+=" order by p.id desc";

        Query query = em.createQuery(editedQuery);
        query.setMaxResults(orderFilters.getSize());
        query.setFirstResult(orderFilters.getPage()*orderFilters.getSize());
        if(orderFilters.getStartDate().isEmpty()){
            query.setParameter("startDate", ZonedDateTime.parse(Constants.MIN_DATE));
        } else {
            query.setParameter("startDate", ZonedDateTime.parse(orderFilters.getStartDate()));
        }
        if(orderFilters.getEndDate().isEmpty()){
            query.setParameter("endDate", ZonedDateTime.now());
        } else {
            query.setParameter("endDate", ZonedDateTime.parse(orderFilters.getEndDate()));
        }
        if(!Objects.isNull(orderFilters.getOrderId())){
            query.setParameter("orderId", orderFilters.getOrderId());
        }
        if(!orderFilters.getStatus().isEmpty()){
            query.setParameter("status", orderFilters.getStatus());
        }
        return query.getResultList();
    }


    public OrderPojo getById(Integer orderId) {
        Query query = em.createQuery(getByIdQuery);
        query.setParameter("id", orderId);
        try{
            return (OrderPojo)query.getSingleResult();
        } catch (NoResultException e){
            return null;
        }
    }

    public List<OrderPojo> getBetweenDates(ZonedDateTime startDate, ZonedDateTime endDate){
        Query query = em.createQuery(getBetweenDatesQuery);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        List<OrderPojo> orderPojoList = query.getResultList();
        return orderPojoList;
    }

    public Long getTotalCount(OrderFilters orderFilters) {
        String editedQuery = new String(getTotalCountQuery);
        if(!Objects.isNull(orderFilters.getOrderId())){
            editedQuery+=" and p.id=:orderId";
        }
        if(!orderFilters.getStatus().isEmpty()){
            editedQuery+=" and p.status=:status";
        }

        Query query = em.createQuery(editedQuery);
        if(orderFilters.getStartDate().isEmpty()){
            query.setParameter("startDate", ZonedDateTime.parse(Constants.MIN_DATE));
        } else {
            query.setParameter("startDate", ZonedDateTime.parse(orderFilters.getStartDate()));
        }
        if(orderFilters.getEndDate().isEmpty()){
            query.setParameter("endDate", ZonedDateTime.now());
        } else {
            query.setParameter("endDate", ZonedDateTime.parse(orderFilters.getEndDate()));
        }
        if(!Objects.isNull(orderFilters.getOrderId())){
            query.setParameter("orderId", orderFilters.getOrderId());
        }
        if(!orderFilters.getStatus().isEmpty()){
            query.setParameter("status", orderFilters.getStatus());
        }
        return (Long) query.getSingleResult();
    }
}
