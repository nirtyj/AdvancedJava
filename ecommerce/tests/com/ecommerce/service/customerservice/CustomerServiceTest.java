package com.ecommerce.service.customerservice;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.ecommerce.service.model.AccessStatus;
import com.ecommerce.service.model.Customer;

class CustomerServiceTest {

	@Test
	void test() {
		CustomerService service = new CustomerServiceImpl();
		service.addCustomer(new Customer(1, "Nirty", AccessStatus.PRIVILEGED));
		Customer c = service.getCustomer(1);
		assertEquals("Nirty", c.getName());
		assertEquals(1, service.getAllCustomers().size());
	}

}
