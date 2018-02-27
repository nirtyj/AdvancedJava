package com.ecommerce.application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

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
		
		System.out.println("Extract just names with custom generic implementation");
		NameExtractor nameExtractor = new NameExtractor();
		List<String> names = getPropertyList(customers, nameExtractor);
		names.forEach(n -> System.out.println(n));
		
		System.out.println("Extract Id's with Function Interface");
		List<Integer> ids = getPropertyListWithFunction(customers, (s) -> s.getId());
		ids.forEach(n -> System.out.println(n));
		
		System.out.println("Filter with Streams - starts with P");
		List<Customer> namewithP = customers.stream().filter((c) -> c.getName().startsWith("P")).collect(Collectors.toList());
		print(namewithP);
		
		System.out.println("Filter with Streams - starts with A and More than 3 chars");
		List<Customer> namewithA = customers.stream().
						filter((c) -> c.getName().startsWith("A")).
						filter((c) -> c.getName().length()>4).
						collect(Collectors.toList());
		print(namewithA);
		
		System.out.println("Filter with Streams, Maps and peeks - starts with A and More than 3 chars only names - Apple");
		boolean debug = true;	
		List<String> nameStringwithA = customers.stream().parallel().
				peek(c-> 
					{ 
						if(debug)
							System.out.println(" Peek 1 : " + c);
					}).
				filter((c) -> c.getName().startsWith("A")).
				peek(c-> 
				{ 
					if(debug)
						System.out.println(" Peek 2 : " + c);
				}).
				filter((c) -> c.getName().length()>4).
				peek(c-> 
				{ 
					if(debug)
						System.out.println(" Peek 3 : " + c);
				}).
				map((c) -> c.getName()).
				peek(c-> 
				{ 
					if(debug)
						System.out.println(" Peek 4 : " + c);
				}).
				collect(Collectors.toList());
		nameStringwithA.forEach(n -> System.out.println(n));
		
		// streams once collected / terminal statements such as for-each cannot be re-used nor cloned

		System.out.println("Filter with Streams - Custom written collectors- starts with A and More than 3 chars");
		
		Supplier<List<Customer>> supplier = () -> new ArrayList<>();
		BiConsumer<List<Customer>, Customer> accumulator = (list, currStudent) -> list.add(currStudent);
		BiConsumer<List<Customer>, List<Customer>> combiner = (list1, list2) -> list1.addAll(list2);

		namewithA = customers.stream().
						filter((c) -> c.getName().startsWith("A")).
						filter((c) -> c.getName().length()>4).
						collect(supplier, accumulator, combiner);
		
		myPrettyprint(namewithA);
		
		// Another way --> Note the ::
		System.out.println("Filter with Streams - Shortly written Collectors - starts with A and More than 3 chars");
		
		namewithA = customers.stream().
				filter((c) -> c.getName().startsWith("A")).
				filter((c) -> c.getName().length()>4).
				collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
		myPrettyprint(namewithA);
		

		System.out.println("Filter with Streams - Pre provided collecters - grouping by start characters ");
		Map<Character, Long> count = customers.stream().
				collect(Collectors.groupingBy((c) -> c.getName().charAt(0) , Collectors.counting()));
		count.forEach( (k, v) -> System.out.println(k + ":" + v));
	

		System.out.println("Filter with Streams - Pre provided collecters - to Map collector with Identity (returns input argument");
		Map<Integer, Customer> count1 = customers.stream().
				collect(Collectors.toMap(Customer::getId, Function.identity()));
		count1.forEach( (k, v) -> System.out.println(k + ":" + v));
		
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
	
	private static void myPrettyprint(List<Customer> customers) {
		// Note the use of :: - use of consumer interface
		customers.forEach(ECommerceApplication::myPrettyPrint);
	}
	
	private static void myPrettyPrint(Customer c)
	{
		System.out.println(" { " + c.toString() + " } ");
	}

	public static <T, R> List<R> getPropertyList(List<T> input, Extract<T, R> extractor)
	{
		List<R> result = new ArrayList<>();
		for(T s : input)
		{
			result.add(extractor.getProperty(s));
		}
		return result;
	}
	
	public static <T, R> List<R> getPropertyListWithFunction(List<T> input, Function<T, R> extractor)
	{
		List<R> result = new ArrayList<>();
		for(T s : input)
		{
			result.add(extractor.apply(s));
		}
		return result;
	}
	
	public interface Extract<T, R>
	{
		public R getProperty(T s);
	}
	
	public static class NameExtractor implements Extract<Customer, String>
	{
		@Override
		public String getProperty(Customer s) {
			return s.getName();
		}
	}
}
