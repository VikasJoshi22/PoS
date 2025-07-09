package com.increff.pos.dao;

import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.utils.ApiException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
@Transactional(rollbackFor = ApiException.class)
public class OrderItemDao {
    private static final String getAllOrderItemsQuery = "select p from OrderItemPojo p";

    @PersistenceContext
    private EntityManager em;

    public void addOrderItem(OrderItemPojo orderItemPojo){
        em.persist(orderItemPojo);
    }

    public List<OrderItemPojo> getAllOrderItems() {
        Query query = em.createQuery(getAllOrderItemsQuery);
        return query.getResultList();
    }
}
