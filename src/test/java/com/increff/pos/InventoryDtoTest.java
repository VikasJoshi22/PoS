package com.increff.pos;

import com.increff.pos.dao.ClientDao;
import com.increff.pos.dao.InventoryDao;
import com.increff.pos.dao.ProductDao;
import com.increff.pos.dto.InventoryDto;
import com.increff.pos.model.data.InventoryData;
import com.increff.pos.model.data.OperationResponse;
import com.increff.pos.model.form.InventoryForm;
import com.increff.pos.pojo.ClientPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.utils.ApiException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class InventoryDtoTest extends AbstractUnitTest {

    @Autowired
    private InventoryDto inventoryDto;

    @Autowired
    private InventoryDao inventoryDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private ClientDao clientDao;

    @Test
    public void testAdd() throws ApiException {
        // Setup: Create client and product first
        ClientPojo clientPojo = TestHelper.createClientPojo("test-client");
        clientDao.add(clientPojo);

        ProductPojo productPojo = TestHelper.createProductPojo("abc123", clientPojo.getId(), "test-product", 100.0, "http://test.com");
        productDao.add(productPojo);

        // Test: Add inventory
        InventoryForm inventoryForm = TestHelper.createInventoryForm("ABC123", 50);
        inventoryDto.add(inventoryForm);

        // Verify: Check if inventory was added correctly
        InventoryPojo savedInventory = inventoryDao.getByProductId(productPojo.getId());
        assertNotNull("Inventory should be saved", savedInventory);
        assertEquals("Quantity should match", (Integer) 50, savedInventory.getQuantity());
    }

    @Test
    public void testBatchAdd() throws ApiException {
        // Setup: Create client and products
        ClientPojo clientPojo = TestHelper.createClientPojo("test-client");
        clientDao.add(clientPojo);

        ProductPojo product1 = TestHelper.createProductPojo("abc123", clientPojo.getId(), "test-product-1", 100.0, "http://test1.com");
        ProductPojo product2 = TestHelper.createProductPojo("def456", clientPojo.getId(), "test-product-2", 200.0, "http://test2.com");
        productDao.add(product1);
        productDao.add(product2);

        // Test: Batch add inventory
        List<InventoryForm> inventoryForms = new ArrayList<>();
        inventoryForms.add(TestHelper.createInventoryForm("ABC123", 50));
        inventoryForms.add(TestHelper.createInventoryForm("DEF456", 75));

        List<OperationResponse<InventoryForm>> responses = inventoryDto.batchAdd(inventoryForms);

        // Verify: Check responses
        assertEquals("Should have 2 responses", 2, responses.size());
        assertEquals("First response should be successful", "No error", responses.get(0).getMessage());
        assertEquals("Second response should be successful", "No error", responses.get(1).getMessage());

        // Verify: Check if inventories were added
        InventoryPojo savedInventory1 = inventoryDao.getByProductId(product1.getId());
        InventoryPojo savedInventory2 = inventoryDao.getByProductId(product2.getId());
        assertNotNull("First inventory should be saved", savedInventory1);
        assertNotNull("Second inventory should be saved", savedInventory2);
        assertEquals("First quantity should match", (Integer) 50, savedInventory1.getQuantity());
        assertEquals("Second quantity should match", (Integer) 75, savedInventory2.getQuantity());
    }

    @Test
    public void testGetByProductId() throws ApiException {
        // Setup: Create client, product, and inventory
        ClientPojo clientPojo = TestHelper.createClientPojo("test-client");
        clientDao.add(clientPojo);

        ProductPojo productPojo = TestHelper.createProductPojo("abc123", clientPojo.getId(), "test-product", 100.0, "http://test.com");
        productDao.add(productPojo);

        InventoryPojo inventoryPojo = TestHelper.createInventoryPojo(productPojo.getId(), 50);
        inventoryDao.add(inventoryPojo);

        // Test: Get inventory by product ID
        InventoryData inventoryData = inventoryDto.getByProductId(productPojo.getId());

        // Verify: Check result
        assertNotNull("Inventory data should not be null", inventoryData);
        assertEquals("Quantity should match", (Integer) 50, inventoryData.getQuantity());
    }

    @Test
    public void testGetAll() throws ApiException {
        // Setup: Create client, products, and inventories
        ClientPojo clientPojo = TestHelper.createClientPojo("test-client");
        clientDao.add(clientPojo);

        ProductPojo product1 = TestHelper.createProductPojo("abc123", clientPojo.getId(), "test-product-1", 100.0, "http://test1.com");
        ProductPojo product2 = TestHelper.createProductPojo("def456", clientPojo.getId(), "test-product-2", 200.0, "http://test2.com");
        productDao.add(product1);
        productDao.add(product2);

        InventoryPojo inventory1 = TestHelper.createInventoryPojo(product1.getId(), 50);
        InventoryPojo inventory2 = TestHelper.createInventoryPojo(product2.getId(), 75);
        inventoryDao.add(inventory1);
        inventoryDao.add(inventory2);

        // Test: Get all inventories
        List<InventoryData> inventoryDataList = inventoryDto.getAll();

        // Verify: Check results
        assertNotNull("Inventory data list should not be null", inventoryDataList);
        assertEquals("Should have 2 inventories", 2, inventoryDataList.size());
    }

    @Test
    public void testGetByBarcode() throws ApiException {
        // Setup: Create client, product, and inventory
        ClientPojo clientPojo = TestHelper.createClientPojo("test-client");
        clientDao.add(clientPojo);

        ProductPojo productPojo = TestHelper.createProductPojo("abc123", clientPojo.getId(), "test-product", 100.0, "http://test.com");
        productDao.add(productPojo);

        InventoryPojo inventoryPojo = TestHelper.createInventoryPojo(productPojo.getId(), 50);
        inventoryDao.add(inventoryPojo);

        // Test: Get inventory by barcode
        InventoryData inventoryData = inventoryDto.getByBarcode("abc123");

        // Verify: Check result
        assertNotNull("Inventory data should not be null", inventoryData);
        assertEquals("Quantity should match", (Integer) 50, inventoryData.getQuantity());
    }

    @Test
    public void testAddWithInvalidQuantity() {
        // Setup: Create client and product
        ClientPojo clientPojo = TestHelper.createClientPojo("test-client");
        clientDao.add(clientPojo);

        ProductPojo productPojo = TestHelper.createProductPojo("abc123", clientPojo.getId(), "test-product", 100.0, "http://test.com");
        productDao.add(productPojo);

        // Test: Try to add inventory with invalid quantity
        InventoryForm inventoryForm = TestHelper.createInventoryForm("ABC123", -10);

        try {
            inventoryDto.add(inventoryForm);
            fail("Should throw ApiException for invalid quantity");
        } catch (ApiException e) {
            // Expected exception
            assertTrue("Error message should contain quantity validation", e.getMessage().contains("Quantity"));
        }
    }
} 