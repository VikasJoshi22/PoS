package com.increff.pos.dto;

import com.increff.pos.model.data.ClientData;
import com.increff.pos.model.form.ClientForm;
import com.increff.pos.pojo.ClientPojo;

public class DtoHelper {
    public static ClientPojo convertClientFormToClientPojo(ClientForm client){
        ClientPojo c = new ClientPojo();
        c.setName(client.getName().toLowerCase());
        return c;
    }

    public static ClientData convertClientPojoToClientData(ClientPojo clientPojo){
        ClientData clientData = new ClientData();
        clientData.setId(clientPojo.getId());
        clientData.setName(clientPojo.getName());
        return clientData;
    }
}
