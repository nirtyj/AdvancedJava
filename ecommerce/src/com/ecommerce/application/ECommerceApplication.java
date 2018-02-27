package com.ecommerce.application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import org.apache.commons.lang3.StringUtils;

import com.ecommerce.service.customerservice.CustomerService;
import com.ecommerce.service.customerservice.CustomerServiceImpl;
import com.ecommerce.service.model.AccessStatus;
import com.ecommerce.service.model.Customer;

public class ECommerceApplication {

	public static void main(String[] args) {
		CustomerService service = new CustomerServiceImpl();
		service.addCustomer(new Customer(1, "Nirty", AccessStatus.PRIVILEGED));
		service.addCustomer(new Customer(2, "Zebra", AccessStatus.PRIVILEGED));
		service.addCustomer(new Customer(3, "Apple", AccessStatus.PRIVILEGED));
		service.addCustomer(new Customer(4, "Plum", AccessStatus.PRIVILEGED));
		service.addCustomer(new Customer(5, "Cherry", AccessStatus.PRIVILEGED));
		service.addCustomer(new Customer(6, "App", AccessStatus.PRIVILEGED));
		List<Customer> customers = new ArrayList<>(service.getAllCustomers());

		System.out.println("Printing Natural Order");
		print(customers);

		Collections.sort(customers, (Customer o1, Customer o2) -> StringUtils.compare(o1.getName(), o2.getName()));

		System.out.println("Printing Name Sorted");
		print(customers);

		System.out.println("Filtering based on Name starts with N");
		Predicate<Customer> startsWithN = c -> c.getName().startsWith("N");
		print(filter(customers, startsWithN));

		System.out.println("Filtering based on Name starts with A");
		Predicate<Customer> startsWithA = c -> c.getName().startsWith("A");
		print(filter(customers, startsWithA));

		System.out.println("Filtering based on Name starts with A or N");
		List<Predicate<Customer>> listOfPredicate = new ArrayList<>();
		listOfPredicate.add(startsWithA);
		listOfPredicate.add(startsWithN);
		print(filterOr(customers, listOfPredicate));

		System.out.println("Filtering based on Name starts with A and length greater than 4");
		listOfPredicate = new ArrayList<>();
		listOfPredicate.add(startsWithA);
		Predicate<Customer> greaterthan4 = c -> c.getName().length() >= 4;
		listOfPredicate.add(greaterthan4);
		print(filterAnd(customers, listOfPredicate));
	}

	private static List<Customer> filter(List<Customer> customers, Predicate<Customer> filter) {
		List<Customer> filteredList = new ArrayList<Customer>();
		customers.forEach(c -> {
			if (filter.test(c))
				filteredList.add(c);
		});
		return filteredList;
	}

	private static List<Customer> filterOr(List<Customer> customers, List<Predicate<Customer>> filters) {
		List<Customer> filteredList = new ArrayList<Customer>();
		Predicate<Customer> orPredicate = (p) -> false;
		orPredicate = filters.stream().reduce(orPredicate, (f, n) -> f.or(n));
		for (Customer c : customers) {
			if (orPredicate.test(c))
				filteredList.add(c);
		}
		return filteredList;
	}

	private static List<Customer> filterAnd(List<Customer> customers, List<Predicate<Customer>> filters) {
		List<Customer> filteredList = new ArrayList<Customer>();
		Predicate<Customer> andPredicate = (p) -> true;
		andPredicate = filters.stream().reduce(andPredicate, (f, n) -> f.and(n));
		for (Customer c : customers) {
			if (andPredicate.test(c))
				filteredList.add(c);
		}
		return filteredList;
	}

	private static void print(List<Customer> customers) {
		customers.forEach(c -> System.out.println(c.toString()));
	}

}
