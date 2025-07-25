package com.increff.pos.dto;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.dao.ClientDao;
import com.increff.pos.model.data.ClientData;
import com.increff.pos.model.form.ClientForm;
import com.increff.pos.pojo.ClientPojo;
import com.increff.pos.utils.ApiException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ClientDtoTest extends AbstractUnitTest {
    @Autowired
    private ClientDto clientDto;
    @Autowired
    private ClientDao clientDao;

    @Test
    public void testAdd() throws ApiException {
        ClientForm clientForm = new ClientForm();
        clientForm.setName("   Client    ");
        clientDto.add(clientForm);
        ClientPojo clientPojo = clientDao.getAll(0, 1).get(0);
        assertEquals(clientForm.getName(), clientPojo.getName());
    }

    @Test
    public void testGetAll(){
        ClientPojo clientPojo = new ClientPojo();
        clientPojo.setName("client");
        clientDao.add(clientPojo);
        List<ClientData> clientDataList = clientDto.getAll(0,1);
        assertEquals(clientPojo.getName(), clientDataList.get(0).getName());
    }

    @Test
    public void testUpdate() throws ApiException{
        ClientPojo clientPojo = new ClientPojo();
        clientPojo.setName("client");
        clientDao.add(clientPojo);

        ClientForm clientForm = new ClientForm();
        clientForm.setName("    Abcd   ");
        clientDto.update(clientDao.getByName("client").getId(), clientForm);

        assertNotNull(clientDao.getByName("abcd"));
    }

    @Test
    public void testGetById() throws ApiException {
        ClientPojo clientPojo = new ClientPojo();
        clientPojo.setName("client");
        clientDao.add(clientPojo);

        assertEquals("client", clientDto.getById(clientDao.getByName("client").getId()).getName());
    }

    @Test
    public void testGetTotalCount(){
        ClientPojo clientPojo = new ClientPojo();
        clientPojo.setName("client");
        clientDao.add(clientPojo);

        assertEquals(1, (long)clientDto.getTotalCount());
    }

}
