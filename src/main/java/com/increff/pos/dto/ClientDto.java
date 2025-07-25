package com.increff.pos.dto;

import com.increff.pos.api.ClientApi;
import com.increff.pos.model.data.ClientData;
import com.increff.pos.model.form.ClientForm;
import com.increff.pos.pojo.ClientPojo;
import com.increff.pos.utils.ApiException;
import com.increff.pos.utils.UtilMethods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class ClientDto {

    @Autowired
    private ClientApi clientApi;

    public void add(ClientForm client) throws ApiException {
        UtilMethods.normalizeClientForm(client);
        UtilMethods.validateClientForm(client);
        ClientPojo clientPojo = DtoHelper.convertClientFormToClientPojo(client);
        clientApi.add(clientPojo);
    }

    public List<ClientData> getAll(Integer page, Integer size) {
        List<ClientPojo> clientPojoList = clientApi.getAll(page, size);
        return DtoHelper.convertClientPojoListToClientDataList(clientPojoList);
    }

    public void update(Integer id, ClientForm clientForm) throws ApiException{
        UtilMethods.normalizeClientForm(clientForm);
        UtilMethods.validateClientForm(clientForm);
        clientApi.update(id, clientForm.getName());
    }

    public ClientData getById(Integer id) throws ApiException{
        ClientPojo clientPojo = clientApi.getById(id);
        return DtoHelper.convertClientPojoToClientData(clientPojo);
    }

    public Long getTotalCount(){
        return clientApi.getTotalCount();
    }

    public List<String> searchByName(Integer page, Integer size, String name) {
        return clientApi.searchByName(page, size, name);
    }
}
