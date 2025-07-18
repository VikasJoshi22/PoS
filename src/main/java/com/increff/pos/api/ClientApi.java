package com.increff.pos.api;

import com.increff.pos.dao.ClientDao;
import com.increff.pos.pojo.ClientPojo;
import com.increff.pos.utils.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class ClientApi {
    @Autowired
    private ClientDao clientDao;

    public void add(ClientPojo clientPojo) throws ApiException{
        checkName(clientPojo.getName());
        clientDao.add(clientPojo);
    }

    public List<ClientPojo> getAll() {
        List<ClientPojo> clientPojoList = clientDao.getAll();
        return clientPojoList;
    }

    public void delete(Integer id) throws ApiException {
        checkId(id);
        clientDao.delete(id);
    }

    public void update(Integer id, String name) throws ApiException{
        checkName(name);
        checkId(id);
        clientDao.update(id, name);
    }

    public ClientPojo getById(Integer id) throws ApiException{
        ClientPojo clientPojo = clientDao.getById(id);
        if(Objects.isNull(clientPojo)){
            throw new ApiException("id doesn't exists");
        }
        return clientPojo;
    }


    private void checkName(String name) throws ApiException{
        ClientPojo clientPojo = clientDao.getByName(name);
        if(Objects.nonNull(clientPojo)) {
            throw new ApiException("Client '"+ name +"' already exists");
        }
    }

    private void checkId(Integer id) throws ApiException{
        ClientPojo clientPojo = clientDao.getById(id);
        if(clientPojo==null) {
            throw new ApiException("Id doesn't exist");
        }
    }
}
