package com.increff.pos;

import com.increff.pos.dto.ClientDto;
import com.increff.pos.model.data.ClientData;
import com.increff.pos.model.form.ClientForm;
import com.increff.pos.utils.ApiException;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;

public class ClientDtoTest extends AbstractUnitTest {
    @Autowired
    private ClientDto clientDto;

    @Test
    public void getById() throws ApiException {
        ClientForm clientForm = new ClientForm();
        clientForm.setName("client");
        clientDto.add(clientForm);
        ClientData clientData = clientDto.getById(1);
        assertEquals(clientData.getName(), "client");
    }

}
