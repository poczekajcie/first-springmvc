package guru.springframework.controllers;

import guru.springframework.domain.Customer;
import guru.springframework.domain.Product;
import guru.springframework.services.CustomerService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.beans.HasProperty.hasProperty;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class CustomerControllerTest {
    @Mock //Mockito Mock object
    private CustomerService customerService;

    @InjectMocks //setups up controller and injects mock object into it
    private CustomerController customerController;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this); //init controllers and mock
        mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
    }

    @Test
    public void testList() throws Exception {

        List<Customer> customers = new ArrayList<>();
        customers.add(new Customer());
        customers.add(new Customer());

        //specific Mockito interaction, tell stub to return list of products
        when(customerService.listAll()).thenReturn((List) customers);

        mockMvc.perform(get("/customer/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("customer/list"))
                .andExpect(model().attribute("customers", hasSize(2)));
    }

    @Test
    public void testShow() throws Exception {
        Integer id = 1;

        when(customerService.getById(id)).thenReturn(new Customer());

        mockMvc.perform(get("/customer/show/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("customer/show"))
                .andExpect(model().attribute("customer", instanceOf(Customer.class)));
    }

    @Test
    public void testNewProduct() throws Exception {
        Integer id = 1;

        verifyZeroInteractions(customerService);

        mockMvc.perform(get("/customer/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("customer/customerform"))
                .andExpect(model().attribute("customer", instanceOf(Customer.class)));
    }

    @Test
    public void  testSaveOrUpdate() throws Exception {
        Integer id = 1;
        String firstName = "jacek";
        String lastName = "krakowski";
        String email = "email@mail.com";
        String phoneNumber = "553445667";
        String addressLineOne = "armii krajowej";
        String addressLineTwo = "50/30";
        String city = "plock";
        String state = "mazowsze";
        String zipCode = "09-400";

        Customer returnCustomer = new Customer();
        returnCustomer.setId(id);
        returnCustomer.setFirstName(firstName);
        returnCustomer.setLastName(lastName);
        returnCustomer.setEmail(email);
        returnCustomer.setPhoneNumber(phoneNumber);
        returnCustomer.setAddressLineOne(addressLineOne);
        returnCustomer.setAddressLineTwo(addressLineTwo);
        returnCustomer.setCity(city);
        returnCustomer.setState(state);
        returnCustomer.setZipCode(zipCode);

        when(customerService.saveOrUpdate(Matchers.<Customer>any())).thenReturn(returnCustomer);

        mockMvc.perform(post("/customer")
                .param("id", "1")
                .param("firstName", "jacek")
                .param("lastName", "krakowski")
                .param("email", "email@mail.com")
                .param("phoneNumber", "553445667")
                .param("addressLineOne", "armii krajowej")
                .param("addressLineTwo", "50/30")
                .param("city", "plock")
                .param("state", "mazowsze")
                .param("zipCode", "09-400"))
                    .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/customer/show/1"))
                .andExpect(model().attribute("customer", instanceOf(Customer.class)))
                .andExpect(model().attribute("customer", hasProperty("id")))
                .andExpect(model().attribute("customer", hasProperty("firstName")))
                .andExpect(model().attribute("customer", hasProperty("lastName")))
                .andExpect(model().attribute("customer", hasProperty("email")))
                .andExpect(model().attribute("customer", hasProperty("phoneNumber")))
                .andExpect(model().attribute("customer", hasProperty("addressLineOne")))
                .andExpect(model().attribute("customer", hasProperty("addressLineTwo")))
                .andExpect(model().attribute("customer", hasProperty("city")))
                .andExpect(model().attribute("customer", hasProperty("state")))
                .andExpect(model().attribute("customer", hasProperty("zipCode")));

        //verify properties of bound object
        ArgumentCaptor<Customer> boundCustomer = ArgumentCaptor.forClass(Customer.class);
        verify(customerService).saveOrUpdate(boundCustomer.capture());

        assertEquals(id, boundCustomer.getValue().getId());
        assertEquals(firstName, boundCustomer.getValue().getFirstName());
        assertEquals(lastName, boundCustomer.getValue().getLastName());
        assertEquals(email, boundCustomer.getValue().getEmail());
        assertEquals(phoneNumber, boundCustomer.getValue().getPhoneNumber());
        assertEquals(addressLineOne, boundCustomer.getValue().getAddressLineOne());
        assertEquals(addressLineTwo, boundCustomer.getValue().getAddressLineTwo());
        assertEquals(city, boundCustomer.getValue().getCity());
        assertEquals(state, boundCustomer.getValue().getState());
        assertEquals(zipCode, boundCustomer.getValue().getZipCode());
    }

    @Test
    public void testDelete() throws Exception {
        Integer id = 1;

        mockMvc.perform(get("/customer/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/customer/list"));

        verify(customerService, times(1)).delete(id);
    }
}
