package com.increff.pos;

import com.increff.pos.dto.InvoiceDto;
import com.increff.pos.utils.ApiException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class InvoiceDtoTest extends AbstractUnitTest {

    @Autowired
    private InvoiceDto invoiceDto;

    @Test
    public void testInvoiceDtoNotNull() {
        assertNotNull(invoiceDto);
    }

    @Test
    public void testInvoiceDtoClass() {
        assertEquals(InvoiceDto.class, invoiceDto.getClass());
    }
} 