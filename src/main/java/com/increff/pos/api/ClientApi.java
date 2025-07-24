package com.increff.pos.api;

import com.increff.pos.dao.ClientDao;
import com.increff.pos.pojo.ClientPojo;
import com.increff.pos.utils.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Transactional(rollbackFor = ApiException.class)
public class ClientApi {
    @Autowired
    private ClientDao clientDao;

    public void add(ClientPojo clientPojo) throws ApiException{
        checkName(clientPojo.getName());
        clientDao.add(clientPojo);
    }

    public List<ClientPojo> getAll(Integer page, Integer size) {
        List<ClientPojo> clientPojoList = clientDao.getAll(page, size);
        return clientPojoList;
    }

    public void update(Integer id, String name) throws ApiException{
        checkName(name);
        checkId(id);
        clientDao.update(id, name);
    }

    public ClientPojo getById(Integer id) throws ApiException{
        ClientPojo clientPojo = clientDao.getById(id);
        if(Objects.isNull(clientPojo)){
            throw new ApiException("Client id doesn't exists");
        }
        return clientPojo;
    }

    public ClientPojo getByName(String name){
        return clientDao.getByName(name);
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

    public Long getTotalCount(){
        return clientDao.getTotalCount();
    }

    public List<String> searchByName(Integer page, Integer size, String name) {
        return clientDao.searchByName(page, size, name);
    }
}
