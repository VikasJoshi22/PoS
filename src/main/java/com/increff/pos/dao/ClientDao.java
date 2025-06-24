package com.increff.pos.dao;

import com.increff.pos.model.form.ClientForm;
import com.increff.pos.pojo.ClientPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

@Transactional
@Repository
public class ClientDao {

    private static String getAllQuery = "select from ClientPojo";
    private static String deleteQuery = "delete from ClientPojo where id=:id";

    @Autowired
    EntityManager em;

    public void add(ClientPojo client){
        em.persist(client);
    }

    public List<ClientPojo> getAll(){
        TypedQuery<ClientPojo> query = em.createQuery(getAllQuery, ClientPojo.class);
        List<ClientPojo> results = query.getResultList();
        return results;
    }

    public void delete(int id){
        TypedQuery<ClientPojo> query = em.createQuery(deleteQuery, ClientPojo.class);
        query.setParameter("id", id);
        query.executeUpdate();
        return;
    }

    public void update(int id, ClientForm client){
    }
}
