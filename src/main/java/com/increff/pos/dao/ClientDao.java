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
    private static final String deleteQuery = "delete from ClientPojo p where id=:id";
    private static final String update = "update ClientPojo set name=:name where id=:id";
    private static final String getByIdQuery = "select p from ClientPojo p where id=:id";
    private static final String getByNameQuery = "select p from ClientPojo p where name=:name";

    @PersistenceContext
    private EntityManager em;

    public void add(ClientPojo client) {
        em.persist(client);
    }

    public List<ClientPojo> getAll() {
        TypedQuery<ClientPojo> query = em.createQuery(getAllQuery, ClientPojo.class);
        return query.getResultList();
    }

    public void delete(Long id) {
        Query query = em.createQuery(deleteQuery);
        query.setParameter("id", id);
        query.executeUpdate();
    }

    public void update(Long id, String name) {
        Query query = em.createQuery(update);
        query.setParameter("id", id);
        query.setParameter("name", name.toLowerCase());
        query.executeUpdate();
    }

    public ClientPojo get(Long id) {
        Query query = em.createQuery(getByIdQuery);
        query.setParameter("id", id);
        try {
            return (ClientPojo) query.getSingleResult();
        } catch (NoResultException noResultException) {
            return null;
        }
    }

    public ClientPojo getByName(String name){
        Query query = em.createQuery(getByNameQuery);
        query.setParameter("name", name);
        try {
            return (ClientPojo) query.getSingleResult();
        } catch (NoResultException noResultException) {
            return null;
        }
    }
}
