package com.ecommerce.service.customerservice;

import java.util.Collection;

import com.ecommerce.service.model.Customer;

public interface CustomerService {

	Customer getCustomer(Integer id);

	Collection<Customer> getAllCustomers();

	void addCustomer(Customer customer);
}
