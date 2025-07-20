package com.increff.pos.dto;

import com.increff.pos.api.ClientApi;
import com.increff.pos.model.data.ClientData;
import com.increff.pos.model.form.ClientForm;
import com.increff.pos.pojo.ClientPojo;
import com.increff.pos.utils.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class ClientDto {

    @Autowired
    private ClientApi clientApi;

    public void add(ClientForm client) throws ApiException {
        DtoHelper.normalizeClientForm(client);
        DtoHelper.validateClientForm(client);
        ClientPojo clientPojo = DtoHelper.convertClientFormToClientPojo(client);
        clientApi.add(clientPojo);
    }

    public List<ClientData> getAll(Integer page, Integer size) {
        List<ClientPojo> clientPojoList = clientApi.getAll(page, size);
        List<ClientData> clientDataList = new ArrayList<>();

        //Converting clientPojoList To ClientDataList;
        for (ClientPojo clientPojo : clientPojoList) {
            ClientData clientData = DtoHelper.convertClientPojoToClientData(clientPojo);
            clientDataList.add(clientData);
        }
        return clientDataList;
    }

    public void delete(Integer id) throws ApiException {
        clientApi.delete(id);
    }

    public void update(Integer id, ClientForm clientForm) throws ApiException{
        DtoHelper.normalizeClientForm(clientForm);
        DtoHelper.validateClientForm(clientForm);
        clientApi.update(id, clientForm.getName());
    }

    public ClientData getById(Integer id) throws ApiException{
        ClientPojo clientPojo = clientApi.getById(id);
        return DtoHelper.convertClientPojoToClientData(clientPojo);
    }

    public Long getTotalCount(){
        return clientApi.getTotalCount();
    }
}
