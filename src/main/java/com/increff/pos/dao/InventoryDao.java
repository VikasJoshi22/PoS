package com.increff.pos.dao;

import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.utils.ApiException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.List;

@Repository
@Transactional(rollbackFor = ApiException.class)
public class InventoryDao {
    private static String updateQuery = "update InventoryPojo p set p.quantity=:quantity where p.productId=:productId";
    private static String getByProductQuery = "select p from InventoryPojo p where p.productId=:productId";
    private static String getAllQuery = "select p from InventoryPojo p";
    @PersistenceContext
    private EntityManager em;

    public void add(InventoryPojo inventoryPojo){
        em.persist(inventoryPojo);
    }

    public void update(InventoryPojo inventoryPojo){
        Query query = em.createQuery(updateQuery);

        query.setParameter("quantity", inventoryPojo.getQuantity());
        query.setParameter("productId", inventoryPojo.getProductId());

        query.executeUpdate();
    }

    public List<InventoryPojo> getAll(){
        Query query = em.createQuery(getAllQuery, InventoryPojo.class);
        return query.getResultList();
    }

    public InventoryPojo getByProduct(Integer productId){
        Query query = em.createQuery(getByProductQuery);
        query.setParameter("productId", productId);
        try {
            return (InventoryPojo) query.getSingleResult();
        }catch (NoResultException noResultException){
            return null;
        }
    }


}
