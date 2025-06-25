package com.increff.pos.dao;

import com.increff.pos.pojo.ClientPojo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@Transactional
@Repository
public class ClientDao {

    private static final String getAllQuery = "select p from ClientPojo p";
    private static final String deleteQuery = "delete from ClientPojo p where id=:id";
    private static final String update = "update ClientPojo set name=:name where id=:id";

    @PersistenceContext
    private EntityManager em;

    public void add(ClientPojo client){
        em.persist(client);
    }

    public List<ClientPojo> getAll(){
        TypedQuery<ClientPojo> query = em.createQuery(getAllQuery, ClientPojo.class);
        return query.getResultList();
    }

    public void delete(int id){
        Query query = em.createQuery(deleteQuery);
        query.setParameter("id", id);
        query.executeUpdate();
    }

    public void update(int id, String name){
        Query query = em.createQuery(update);
        query.setParameter("id", id);
        query.setParameter("name", name.toLowerCase());
        query.executeUpdate();
    }
}
