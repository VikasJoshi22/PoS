package com.increff.pos.dto;

import com.increff.pos.model.form.ClientForm;
import com.increff.pos.pojo.ClientPojo;

public class DtoHelper {
    public static ClientPojo convert(ClientForm client){
        ClientPojo c = new ClientPojo();
        c.setName(client.getName());
        return c;
    }
}
