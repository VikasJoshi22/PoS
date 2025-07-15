package com.increff.pos.dao;

import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.utils.ApiException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
@Transactional(rollbackFor = ApiException.class)
public class OrderDao {
    private static final String getAllOrdersQuery = "select p from OrderPojo p";
    private static final String getByIdQuery = "select p from OrderPojo p where id=:id";
    private static final String updateQuery = "update OrderPojo p set p.dateTime=:dateTime, p.status=:status where id=:id";


    @PersistenceContext
    private EntityManager em;

    public void addOrder(OrderPojo orderPojo){
        em.persist(orderPojo);
    }

    public void update(Integer id, OrderPojo orderPojo){
        Query query = em.createQuery(updateQuery);
        query.setParameter("dateTime", orderPojo.getDateTime());
        query.setParameter("status", orderPojo.getStatus());
        query.setParameter("id", id);

        query.executeUpdate();
    }


    public List<OrderPojo> getAllOrders() {
        Query query = em.createQuery(getAllOrdersQuery);
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
}
