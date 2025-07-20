package com.increff.pos.dao;

import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.utils.ApiException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.List;

@Repository
@Transactional(rollbackFor = ApiException.class)
public class ProductDao {
    private static final String getAllQuery = "select p from ProductPojo p";
    private static final String getByBarcodeQuery = "select p from ProductPojo p where barcode=:barcode";
    private static final String getByIdQuery = "select p from ProductPojo p where id=:id";
    private static final String updateQuery = "update ProductPojo p set p.barcode=:barcode, p.clientId=:clientId, p.name=:name, p.mrp=:mrp, p.imageUrl=:imageUrl where id=:id";
    private static final String getTotalCountQuery = "select count(p) from ProductPojo p";

    @PersistenceContext
    private EntityManager em;

    public void add(ProductPojo productPojo){
        em.persist(productPojo);
    }

    public void batchAdd(List<ProductPojo> productPojoList){
        for(ProductPojo productPojo: productPojoList){
            em.persist(productPojo);
        }
    }

    public List<ProductPojo> getAll(Integer page, Integer size, String keyword){
        String newQuery = new String(getAllQuery);
        if(!keyword.isEmpty()){
            newQuery+=" where p.barcode like :keyword or p.name like :keyword";
        }
        TypedQuery<ProductPojo> query = em.createQuery(newQuery, ProductPojo.class);
        if(!keyword.isEmpty()){
            keyword = "%"+keyword.toLowerCase().trim()+"%";
            query.setParameter("keyword", keyword);
        }
        query.setFirstResult(page*size);
        query.setMaxResults(size);

        return query.getResultList();
    }



    public ProductPojo getByBarcode(String barcode){
        Query query = em.createQuery(getByBarcodeQuery);
        query.setParameter("barcode", barcode);
        try{
            return (ProductPojo) query.getSingleResult();
        }catch (NoResultException noResultException){
            return null;
        }
    }

    public void update(Integer id, ProductPojo productPojo){
        Query query = em.createQuery(updateQuery);

        query.setParameter("id", id);
        query.setParameter("name", productPojo.getName());
        query.setParameter("clientId", productPojo.getClientId());
        query.setParameter("mrp", productPojo.getMrp());
        query.setParameter("imageUrl", productPojo.getImageUrl());
        query.setParameter("barcode", productPojo.getBarcode());
        query.executeUpdate();
    }

    public ProductPojo getById(Integer id) throws ApiException {
        Query query = em.createQuery(getByIdQuery);
        query.setParameter("id", id);
        try{
            return (ProductPojo) query.getSingleResult();
        }catch (NoResultException noResultException){
            return null;
        }
    }

    public Long getTotalCount(){
        Query query = em.createQuery(getTotalCountQuery);
        return (Long) query.getSingleResult();
    }

}
