package com.increff.pos.dao;

import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.utils.ApiException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
@Transactional(rollbackFor = ApiException.class)
public class ProductDao {
    @PersistenceContext
    private EntityManager em;

    public void add(ProductPojo productPojo){
        em.persist(productPojo);
    }
}
