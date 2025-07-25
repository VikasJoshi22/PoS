package com.increff.pos.dto;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.TestHelper;
import com.increff.pos.dao.ClientDao;
import com.increff.pos.dao.InventoryDao;
import com.increff.pos.dao.ProductDao;
import com.increff.pos.model.data.OperationResponse;
import com.increff.pos.model.data.ProductData;
import com.increff.pos.model.form.ProductForm;
import com.increff.pos.pojo.ClientPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.utils.ApiException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ProductDtoTest extends AbstractUnitTest {

    @Autowired
    private ProductDto productDto;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private ClientDao clientDao;

    @Autowired
    private InventoryDao inventoryDao;

    @Test
    public void testAdd() throws ApiException {
        ClientPojo clientPojo = TestHelper.createClientPojo("test-client");
        clientDao.add(clientPojo);

        ProductForm productForm = TestHelper.createProductForm("abc123", "test-client", "test-product", 100.0, "http://test.com");
        productDto.add(productForm);

        ProductPojo savedProduct = productDao.getByBarcode("abc123");
        assertNotNull(savedProduct);
        assertEquals("abc123", savedProduct.getBarcode());
        assertEquals("test-product", savedProduct.getName());
        assertEquals((Double) 100.0, savedProduct.getMrp());
    }

    @Test
    public void testGetAll() throws ApiException {
        ClientPojo clientPojo = TestHelper.createClientPojo("test-client");
        clientDao.add(clientPojo);

        ProductPojo productPojo1 = TestHelper.createProductPojo("abc123", clientPojo.getId(), "product-1", 100.0, "http://test1.com");
        ProductPojo productPojo2 = TestHelper.createProductPojo("def456", clientPojo.getId(), "product-2", 200.0, "http://test2.com");
        productDao.add(productPojo1);
        productDao.add(productPojo2);

        List<ProductData> result = productDto.getAll(0, 10, "");

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("abc123", result.get(0).getBarcode());
        assertEquals("def456", result.get(1).getBarcode());
    }

    @Test
    public void testGetById() throws ApiException {
        ClientPojo clientPojo = TestHelper.createClientPojo("test-client");
        clientDao.add(clientPojo);

        ProductPojo productPojo = TestHelper.createProductPojo("abc123", clientPojo.getId(), "test-product", 100.0, "http://test.com");
        productDao.add(productPojo);

        ProductData result = productDto.getById(productPojo.getId());

        assertNotNull(result);
        assertEquals(productPojo.getId(), result.getId());
        assertEquals("abc123", result.getBarcode());
        assertEquals("test-product", result.getName());
    }

    @Test
    public void testGetByBarcode() throws ApiException {
        ClientPojo clientPojo = TestHelper.createClientPojo("test-client");
        clientDao.add(clientPojo);

        ProductPojo productPojo = TestHelper.createProductPojo("abc123", clientPojo.getId(), "test-product", 100.0, "http://test.com");
        productDao.add(productPojo);

        ProductData result = productDto.getByBarcode("abc123");

        assertNotNull(result);
        assertEquals("abc123", result.getBarcode());
        assertEquals("test-product", result.getName());
    }

    @Test
    public void testGetTotalCount() {
        ClientPojo clientPojo = TestHelper.createClientPojo("test-client");
        clientDao.add(clientPojo);

        ProductPojo productPojo1 = TestHelper.createProductPojo("abc123", clientPojo.getId(), "product-1", 100.0, "http://test1.com");
        ProductPojo productPojo2 = TestHelper.createProductPojo("def456", clientPojo.getId(), "product-2", 200.0, "http://test2.com");
        productDao.add(productPojo1);
        productDao.add(productPojo2);

        Long result = productDto.getTotalCount();

        assertNotNull(result);
        assertEquals(2L, result.longValue());
    }

    @Test
    public void testSearchByBarcode() {
        ClientPojo clientPojo = TestHelper.createClientPojo("test-client");
        clientDao.add(clientPojo);

        ProductPojo productPojo1 = TestHelper.createProductPojo("abc123", clientPojo.getId(), "product-1", 100.0, "http://test1.com");
        ProductPojo productPojo2 = TestHelper.createProductPojo("abc456", clientPojo.getId(), "product-2", 200.0, "http://test2.com");
        productDao.add(productPojo1);
        productDao.add(productPojo2);

        List<String> result = productDto.searchByBarcode(0, 10, "abc");

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains("abc123"));
        assertTrue(result.contains("abc456"));
    }

    @Test
    public void testBatchAdd() throws ApiException {
        ClientPojo clientPojo = TestHelper.createClientPojo("test-client");
        clientDao.add(clientPojo);

        List<ProductForm> productForms = new ArrayList<>();
        productForms.add(TestHelper.createProductForm("abc123", "test-client", "product-1", 100.0, "http://test1.com"));
        productForms.add(TestHelper.createProductForm("def456", "test-client", "product-2", 200.0, "http://test2.com"));

        List<OperationResponse<ProductForm>> result = productDto.batchAdd(productForms);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("No error", result.get(0).getMessage());
        assertEquals("No error", result.get(1).getMessage());
    }

    @Test
    public void testBatchAddWithInvalidData() {
        List<ProductForm> productForms = new ArrayList<>();
        productForms.add(TestHelper.createProductForm("abc123", "non-existent-client", "product-1", 100.0, "http://test1.com"));

        List<OperationResponse<ProductForm>> result = productDto.batchAdd(productForms);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertNotEquals("No error", result.get(0).getMessage());
    }
}
