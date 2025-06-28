package com.increff.pos.dto;

import com.increff.pos.dao.ClientDao;
import com.increff.pos.model.data.ClientData;
import com.increff.pos.model.form.ClientForm;
import com.increff.pos.pojo.ClientPojo;
import com.increff.pos.utils.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import com.increff.pos.dto.DtoHelper;

import java.util.ArrayList;
import java.util.List;


@Component
public class ClientDto {

    @Autowired
    private ClientDao clientDao;

    public void add(ClientForm client) throws ApiException {
        checkName(client.getName());
        ClientPojo c = DtoHelper.convertClientFormToClientPojo(client);
        clientDao.add(c);
    }

    public List<ClientData> getAll() {
        List<ClientPojo> clientPojoList = clientDao.getAll();
        List<ClientData> clientDataList = new ArrayList<ClientData>();

        //Converting clientPojoList To ClientDataList;
        for (ClientPojo clientPojo : clientPojoList) {
            ClientData clientData = DtoHelper.convertClientPojoToClientData(clientPojo);
            clientDataList.add(clientData);
        }
        return clientDataList;
    }

    public void delete(Long id) throws ApiException {
        checkId(id);
        clientDao.delete(id);
    }

    public void update(Long id, ClientForm clientForm) throws ApiException{
        checkId(id);
        clientDao.update(id, clientForm.getName());
    }

    public ClientData get(Long id) throws ApiException{
        checkId(id);
        ClientPojo clientPojo = clientDao.get(id);
        return DtoHelper.convertClientPojoToClientData(clientPojo);
    }

    public void checkId(Long id) throws ApiException{
        ClientPojo clientPojo = clientDao.get(id);
        if(clientPojo==null) {
            throw new ApiException("Id doesn't exist");
        }
    }

    public void checkName(String name) throws ApiException{
        ClientPojo clientPojo = clientDao.getByName(name);
        if(clientPojo!=null) {
            throw new ApiException("Client already exists");
        }
    }
}
