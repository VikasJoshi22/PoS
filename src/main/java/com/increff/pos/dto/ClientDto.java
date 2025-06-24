package com.increff.pos.dto;

import com.increff.pos.dao.ClientDao;
import com.increff.pos.model.form.ClientForm;
import com.increff.pos.pojo.ClientPojo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.increff.pos.dto.DtoHelper.convert;

public class ClientDto {

    @Autowired
    private ClientDao clientDao;

    public List<ClientPojo> update(int id, String client){
        return null;
    }

    public void add(ClientForm client) {
        ClientPojo c = convert(client);
        clientDao.add(c);
    }

    public List<ClientPojo> getAll() {
        return null;
    }

    public void delete(int id) {

    }
}
