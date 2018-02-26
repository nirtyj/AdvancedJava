package com.ecommerce.service.customerservice;

import java.util.Collection;
import java.util.HashMap;

import com.ecommerce.service.model.Customer;

public class CustomerServiceImpl implements CustomerService {

	HashMap<Integer, Customer> customers = new HashMap<Integer, Customer>();
	
	@Override
	public Customer getCustomer(Integer id) {	
		return customers.get(id);
	}

	@Override
	public Collection<Customer> getAllCustomers() {
		return customers.values();
	}

	@Override
	public void addCustomer(Customer customer) {
		customers.put(customer.getId(), customer);
	}

}
