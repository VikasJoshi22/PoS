package com.increff.pos;

import com.increff.pos.dao.ClientDao;
import com.increff.pos.dao.ProductDao;
import com.increff.pos.dto.DtoHelper;
import com.increff.pos.dto.ProductDto;
import com.increff.pos.model.data.ProductData;
import com.increff.pos.model.form.ProductForm;
import com.increff.pos.pojo.ClientPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.utils.ApiException;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class ProductDtoTest extends AbstractUnitTest{
    @Autowired
    private ProductDto productDto;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private ClientDao clientDao;

    @Test
    public void testAdd() throws ApiException {
        ClientPojo clientPojo = new ClientPojo();
        clientPojo.setName("client");
        clientDao.add(clientPojo);

        ProductForm productForm = new ProductForm();
        productForm.setClientName("      client        ");
        productForm.setBarcode("        Abc123      ");
        productForm.setMrp(100.40);
        productForm.setName("    Product-1    ");
        productForm.setImageUrl("http://abc.com");
        productDto.add(productForm);

        ProductPojo productPojo = productDao.getByBarcode("abc123");
        assertEquals(clientDao.getByName("client").getId(), productPojo.getClientId());
        assertEquals("abc123", productPojo.getBarcode());
        assertEquals((Double) 100.40, productPojo.getMrp());
        assertEquals("product-1", productPojo.getName());
        assertEquals("http://abc.com", productPojo.getImageUrl());
    }

    @Test
    public void testGetAll() throws ApiException {
        ClientPojo clientPojo = new ClientPojo();
        clientPojo.setName("client");
        clientDao.add(clientPojo);

        ProductPojo productPojo = new ProductPojo();
        productPojo.setClientId(clientDao.getByName("client").getId());
        productPojo.setBarcode("abc123");
        productPojo.setMrp(100.40);
        productPojo.setName("product-1");
        productPojo.setImageUrl("http://abc.com");
        productDao.add(productPojo);

        ProductData actual = productDto.getAll(0,1, "").get(0);
        assertEquals("abc123", actual.getBarcode());
        assertEquals("client", actual.getClientName());
        assertEquals("product-1", actual.getName());
        assertEquals((Double) 100.40, actual.getMrp());
        assertEquals("http://abc.com", actual.getImageUrl());
    }
}
