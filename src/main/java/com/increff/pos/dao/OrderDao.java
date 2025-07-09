package com.increff.pos.dao;

import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.utils.ApiException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
@Transactional(rollbackFor = ApiException.class)
public class OrderDao {
    private static final String getAllOrdersQuery = "select p from OrderPojo p";


    @PersistenceContext
    private EntityManager em;

    public void addOrder(OrderPojo orderPojo){
        em.persist(orderPojo);
    }




    public List<OrderPojo> getAllOrders() {
        Query query = em.createQuery(getAllOrdersQuery);
        return query.getResultList();
    }



}
