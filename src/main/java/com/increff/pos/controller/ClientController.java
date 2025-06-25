package com.increff.pos.controller;
import com.increff.pos.dto.ClientDto;
import com.increff.pos.model.data.ClientData;
import com.increff.pos.model.form.ClientForm;
import com.increff.pos.pojo.ClientPojo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
@RequestMapping("/api/client")
public class ClientController {

    @Autowired
    private ClientDto clientDto;

    @ApiOperation("adds a client")
    @RequestMapping(path = "/add", method = RequestMethod.POST)
    public void add(@RequestBody ClientForm client){
        clientDto.add(client);
    }

    @ApiOperation("gets all the client")
    @RequestMapping(path = "/all", method = RequestMethod.GET)
    public List<ClientData> getAll(){
        return clientDto.getAll();
    }

    @ApiOperation("Deletes a client")
    @RequestMapping(path = "/delete/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable int id){
        clientDto.delete(id);
    }

    @ApiOperation("updates a client")
    @RequestMapping(path = "/update/{id}", method = RequestMethod.PUT)
    public void update(@PathVariable int id,  @RequestBody ClientForm client){
        clientDto.update(id, client);
    }

//    @RequestMapping(path = "/get" , method = RequestMethod.GET)
//    public void get(@RequestBody int id){
//
//    }
}
