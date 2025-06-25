package com.increff.pos.dto;

import com.increff.pos.dao.ClientDao;
import com.increff.pos.model.data.ClientData;
import com.increff.pos.model.form.ClientForm;
import com.increff.pos.pojo.ClientPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.increff.pos.dto.DtoHelper;

import java.util.ArrayList;
import java.util.List;


@Component
public class ClientDto {

    @Autowired
    private ClientDao clientDao;

    public void add(ClientForm client) {
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

    public void delete(int id) {
        clientDao.delete(id);
    }

    public void update(int id, ClientForm clientForm){
        clientDao.update(id, clientForm.getName());
        return;
    }
}
