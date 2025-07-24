package com.increff.pos.dao;

import com.increff.pos.pojo.ClientPojo;
import com.increff.pos.utils.ApiException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.List;

@Transactional(rollbackFor = ApiException.class)
@Repository
public class ClientDao {

    private static final String getAllQuery = "select p from ClientPojo p";
    private static final String update = "update ClientPojo set name=:name where id=:id";
    private static final String getByIdQuery = "select p from ClientPojo p where id=:id";
    private static final String getByNameQuery = "select p from ClientPojo p where name=:name";
    private static final String getTotalCountQuery = "select count(p) from ClientPojo p";
    private static final String searchByBarcodeQuery = "select p.name from ClientPojo p where p.name like :name";

    @PersistenceContext
    private EntityManager em;

    public void add(ClientPojo client) {
        em.persist(client);
    }

    public List<ClientPojo> getAll(Integer page, Integer size) {
        Query query = em.createQuery(getAllQuery);
        query.setFirstResult(page*size);
        query.setMaxResults(size);
        return query.getResultList();
    }

    public void update(Integer id, String name) {
        Query query = em.createQuery(update);
        query.setParameter("id", id);
        query.setParameter("name", name);
        query.executeUpdate();
    }

    public ClientPojo getById(Integer id) {
        Query query = em.createQuery(getByIdQuery);
        query.setParameter("id", id);
        try {
            return (ClientPojo) query.getSingleResult();
        } catch (NoResultException noResultException) {
            return null;
        }
    }

    public ClientPojo getByName(String name) {
        Query query = em.createQuery(getByNameQuery);
        query.setParameter("name", name);
        try {
            return (ClientPojo) query.getSingleResult();
        } catch (NoResultException noResultException) {
            return null;
        }
    }

    public Long getTotalCount(){
        Query query = em.createQuery(getTotalCountQuery);
        return (Long) query.getSingleResult();
    }

    public List<String> searchByName(Integer page, Integer size, String name) {
        Query query = em.createQuery(searchByBarcodeQuery);
        query.setParameter("name", "%"+name+"%");
        query.setFirstResult(page*size);
        query.setMaxResults(size);
        return query.getResultList();
    }
}
