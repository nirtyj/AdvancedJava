package com.ecommerce.service.model;

public class Customer implements Comparable<Customer>{

	private Integer id;
	private String name;
	private AccessStatus status;

	public Customer(Integer id, String name, AccessStatus status) {
		this.id = id;
		this.name = name;
		this.status = status;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public AccessStatus getStatus() {
		return status;
	}

	public void setStatus(AccessStatus status) {
		this.status = status;
	}

	@Override
	public int compareTo(Customer otherCustomer) {
		return Integer.compare(this.id, otherCustomer.id);
	}

	@Override
	public String toString() {
		return "Customer [id=" + id + ", name=" + name + ", status=" + status + "]";
	}
}
