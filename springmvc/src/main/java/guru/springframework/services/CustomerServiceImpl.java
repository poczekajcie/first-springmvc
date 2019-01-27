package guru.springframework.services;

import guru.springframework.domain.Customer;
import guru.springframework.domain.DomainObject;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CustomerServiceImpl extends AbstractMapService implements CustomerService {

    @Override
    public List<DomainObject> listAll() {
        return super.listAll();
    }

    @Override
    public Customer getById(Integer id) {
        return (Customer) super.getById(id);
    }

    @Override
    public Customer saveOrUpdate(Customer domainObject) {
        return (Customer) super.saveOrUpdate(domainObject);
    }

    @Override
    public void delete(Integer id) {
        super.delete(id);
    }

    @Override
    protected void loadDomainObjects() {
        domainMap = new HashMap<>();

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
        domainMap.put(1, customer1);

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
        domainMap.put(2, customer2);
    }
}
