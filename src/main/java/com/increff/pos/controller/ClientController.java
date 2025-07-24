package com.increff.pos.controller;
import com.increff.pos.dto.ClientDto;
import com.increff.pos.model.data.ClientData;
import com.increff.pos.model.form.ClientForm;
import com.increff.pos.utils.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
@RequestMapping("/api/clients/supervisor") //TODO: no need to add supervisor in url
public class ClientController {

    @Autowired
    private ClientDto clientDto;


//todo: no need to add extra paths, it can overload based on requestMethod
    @ApiOperation("adds a client")
    @RequestMapping(path = "/add", method = RequestMethod.POST)
    public void add(@RequestBody ClientForm client) throws ApiException {
        clientDto.add(client);
    }

    @ApiOperation("gets all the client")
    @RequestMapping(path = "/get-all", method = RequestMethod.GET)
    public List<ClientData> getAll(@RequestParam Integer page, @RequestParam Integer size){
        return clientDto.getAll(page, size);
    }

    // todo: remove delete
    @ApiOperation("Deletes a client")
    @RequestMapping(path = "/delete/{id}", method = RequestMethod.DELETE)
    public void delete (@PathVariable Integer id) throws ApiException{
        clientDto.delete(id);
    }

    @ApiOperation("updates a client")
    @RequestMapping(path = "/update/{id}", method = RequestMethod.PUT)
    public void update(@PathVariable Integer id,  @RequestBody ClientForm client) throws ApiException{
        clientDto.update(id, client);
    }

    @RequestMapping(path = "/get/{id}" , method = RequestMethod.GET)
    public ClientData getById(@PathVariable Integer id) throws ApiException{
        return clientDto.getById(id);
    }

    @RequestMapping(path = "/get-total-count", method = RequestMethod.GET)
    public Long getTotalCount(){
        return clientDto.getTotalCount();
    }

    // todo: add request method
    @RequestMapping(path = "/search-by-name")
    public List<String> searchByName(@RequestParam Integer page, @RequestParam Integer size, @RequestParam String name){
        return clientDto.searchByName(page, size, name);
    }
}
