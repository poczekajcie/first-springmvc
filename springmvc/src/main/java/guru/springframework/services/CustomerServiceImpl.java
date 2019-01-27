package guru.springframework.services;

import guru.springframework.domain.Customer;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CustomerServiceImpl implements CustomerService {

    private Map<Integer,Customer> customers;

    public CustomerServiceImpl() {
        loadCustomers();
    }

    @Override
    public List<Customer> listAllCustomers() {
        return new ArrayList<>(customers.values());
    }

    @Override
    public Customer getCustomerById(Integer id) {
        return customers.get(id);
    }

    @Override
    public Customer saveOrUpdateCustomer(Customer customer) {
        if (customer != null) {
            if (customer.getId() == null) {
                customer.setId(getNextKey());
            }
            customers.put(customer.getId(), customer);

            return customer;
        } else {
            throw new RuntimeException("Customer can't be null");
        }
    }

    @Override
    public void deleteCustomer(Integer id) {
        customers.remove(id);
    }

    private Integer getNextKey() {
        return Collections.max(customers.keySet()) + 1;
    }

    private void loadCustomers() {
        customers = new HashMap<>();

        Customer customer1 = new Customer();
        customer1.setId(1);
        customer1.setFirstName("Jacek");
        customer1.setLastName("Krakowski");
        customer1.setEmail("krakowski.jacek97@wp.pl");
        customer1.setPhoneNumber("557175324");
        customer1.setAddressLineOne("Armii krajowej");
        customer1.setAddressLineTwo("50/30");
        customer1.setCity("Płock");
        customer1.setState("Mazowsze");
        customer1.setZipCode("09-410");
        customers.put(1, customer1);

        Customer customer2 = new Customer();
        customer2.setId(2);
        customer2.setFirstName("Jan");
        customer2.setLastName("Kowalski");
        customer2.setEmail("krakowskijacek@wp.pl");
        customer2.setPhoneNumber("732456732");
        customer2.setAddressLineOne("Płocka");
        customer2.setAddressLineTwo("50/11");
        customer2.setCity("Sierpc");
        customer2.setState("Mazowsze");
        customer2.setZipCode("09-200");
        customers.put(2, customer2);

    }
}
