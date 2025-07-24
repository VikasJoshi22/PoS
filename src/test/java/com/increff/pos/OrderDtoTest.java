package com.increff.pos;

import com.increff.pos.dao.ClientDao;
import com.increff.pos.dao.InventoryDao;
import com.increff.pos.dao.OrderDao;
import com.increff.pos.dao.OrderItemDao;
import com.increff.pos.dao.ProductDao;
import com.increff.pos.dto.OrderDto;
import com.increff.pos.model.data.ErrorData;
import com.increff.pos.model.data.OrderData;
import com.increff.pos.model.data.OrderError;
import com.increff.pos.model.form.OrderFilters;
import com.increff.pos.model.form.OrderForm;
import com.increff.pos.pojo.ClientPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.utils.ApiException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class OrderDtoTest extends AbstractUnitTest {

    @Autowired
    private OrderDto orderDto;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderItemDao orderItemDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private ClientDao clientDao;

    @Autowired
    private InventoryDao inventoryDao;

    @Test
    public void testCreate() throws ApiException {
        ClientPojo clientPojo = TestHelper.createClientPojo("test-client");
        clientDao.add(clientPojo);

        ProductPojo productPojo = TestHelper.createProductPojo("abc123", clientPojo.getId(), "test-product", 100.0, "http://test.com");
        productDao.add(productPojo);

        InventoryPojo inventoryPojo = TestHelper.createInventoryPojo(productPojo.getId(), 50);
        inventoryDao.add(inventoryPojo);

        List<OrderForm> orderForms = TestHelper.createOrderFormList("abc123", 10, 80.0);

        ErrorData<OrderError> result = orderDto.create(orderForms);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(0, result.getErrorList().size());
    }

    @Test
    public void testCreateWithInvalidBarcode() {
        List<OrderForm> orderForms = TestHelper.createOrderFormList("invalid-barcode", 10, 80.0);

        ErrorData<OrderError> result = orderDto.create(orderForms);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(1, result.getErrorList().size());
        assertEquals("invalid-barcode", result.getErrorList().get(0).getBarcode());
    }

    @Test
    public void testGetOrderDetails() throws ApiException {
        ClientPojo clientPojo = TestHelper.createClientPojo("test-client");
        clientDao.add(clientPojo);

        ProductPojo productPojo = TestHelper.createProductPojo("abc123", clientPojo.getId(), "test-product", 100.0, "http://test.com");
        productDao.add(productPojo);

        OrderPojo orderPojo = TestHelper.createOrderPojo();
        orderDao.addOrder(orderPojo);

        OrderItemPojo orderItemPojo = TestHelper.createOrderItemPojo(orderPojo.getId(), productPojo.getId(), 10, 80.0);
        orderItemDao.addOrderItem(orderItemPojo);

        OrderData result = orderDto.getOrderDetails(orderPojo.getId());

        assertNotNull(result);
        assertEquals(orderPojo.getId(), result.getId());
        assertEquals("created", result.getStatus());
        assertEquals(1, result.getOrderItems().size());
    }

    @Test
    public void testMakeOrderInvoiced() throws ApiException {
        OrderPojo orderPojo = TestHelper.createOrderPojo();
        orderDao.addOrder(orderPojo);

        orderDto.makeOrderInvoiced(orderPojo.getId());

        OrderPojo updatedOrder = orderDao.getById(orderPojo.getId());
        assertEquals("invoiced", updatedOrder.getStatus());
    }

    @Test
    public void testGetAll() throws ApiException {
        ClientPojo clientPojo = TestHelper.createClientPojo("test-client");
        clientDao.add(clientPojo);

        ProductPojo productPojo = TestHelper.createProductPojo("abc123", clientPojo.getId(), "test-product", 100.0, "http://test.com");
        productDao.add(productPojo);

        OrderPojo orderPojo1 = TestHelper.createOrderPojo();
        OrderPojo orderPojo2 = TestHelper.createOrderPojo();
        orderDao.addOrder(orderPojo1);
        orderDao.addOrder(orderPojo2);

        OrderItemPojo orderItemPojo1 = TestHelper.createOrderItemPojo(orderPojo1.getId(), productPojo.getId(), 10, 80.0);
        OrderItemPojo orderItemPojo2 = TestHelper.createOrderItemPojo(orderPojo2.getId(), productPojo.getId(), 15, 85.0);
        orderItemDao.addOrderItem(orderItemPojo1);
        orderItemDao.addOrderItem(orderItemPojo2);

        OrderFilters filters = TestHelper.createOrderFilters(0, 10, "2020-01-01T00:00:00Z", "2030-12-31T23:59:59Z");

        List<OrderData> result = orderDto.getAll(filters);

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void testGetTotalCount() {
        OrderPojo orderPojo1 = TestHelper.createOrderPojo();
        OrderPojo orderPojo2 = TestHelper.createOrderPojo();
        orderDao.addOrder(orderPojo1);
        orderDao.addOrder(orderPojo2);

        OrderFilters filters = TestHelper.createOrderFilters(0, 10, "2020-01-01T00:00:00Z", "2030-12-31T23:59:59Z");

        Long result = orderDto.getTotalCount(filters);

        assertNotNull(result);
        assertEquals(2L, result.longValue());
    }
} 