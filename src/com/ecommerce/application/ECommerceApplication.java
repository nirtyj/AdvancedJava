package com.ecommerce.application;

import com.ecommerce.service.customerservice.CustomerService;
import com.ecommerce.service.customerservice.CustomerServiceImpl;
import com.ecommerce.service.model.AccessStatus;
import com.ecommerce.service.model.Customer;

public class ECommerceApplication {

	public static void main(String[] args) {
		
		CustomerService service = new CustomerServiceImpl();
		service.addCustomer(new Customer(1, "Nirty", AccessStatus.PRIVILEGED));
		

	}

}
