package guru.springframework.controllers;

import guru.springframework.domain.Product;
import guru.springframework.services.ProductService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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

public class ProductControllerTest {

    @Mock //Mockito Mock object
    private ProductService productService;

    @InjectMocks //setups up controller and injects mock object into it
    private ProductController productController;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this); //init controllers and mock
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    @Test
    public void testList() throws Exception {

        List<Product> products = new ArrayList<>();
        products.add(new Product());
        products.add(new Product());

        //specific Mockito interaction, tell stub to return list of products
        when(productService.listAll()).thenReturn((List) products);

        mockMvc.perform(get("/product/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("product/list"))
                .andExpect(model().attribute("products", hasSize(2)));
    }

    @Test
    public void testShow() throws Exception {
        Integer id = 1;

        when(productService.getById(id)).thenReturn(new Product());

        mockMvc.perform(get("/product/show/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("product/show"))
                .andExpect(model().attribute("product", instanceOf(Product.class)));
    }

    @Test
    public void testNewProduct() throws Exception {
        Integer id = 1;

        verifyZeroInteractions(productService);

        mockMvc.perform(get("/product/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("product/productform"))
                .andExpect(model().attribute("product", instanceOf(Product.class)));
    }

    @Test
    public void  testSaveOrUpdate() throws Exception {
        Integer id = 1;
        String description = "Test description";
        double price = 12.00;
        String imageURL = "example.com";

        Product returnProduct = new Product();
        returnProduct.setId(id);
        returnProduct.setDescription(description);
        returnProduct.setPrice(price);
        returnProduct.setImageURL(imageURL);

        when(productService.saveOrUpdate(org.mockito.Matchers.<Product>any())).thenReturn(returnProduct);

        mockMvc.perform(post("/product")
                .param("id", "1")
                .param("description", description)
                .param("price", "12.00")
                .param("imageURL", "example.com"))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(view().name("redirect:/product/show/1"))
                    .andExpect(model().attribute("product", instanceOf(Product.class)))
                    .andExpect(model().attribute("product", hasProperty("id")))
                    .andExpect(model().attribute("product", hasProperty("description")))
                    .andExpect(model().attribute("product", hasProperty("price")))
                    .andExpect(model().attribute("product", hasProperty("imageURL")));

        //verify properites of bound object
        ArgumentCaptor<Product> boundProduct = ArgumentCaptor.forClass(Product.class);
        verify(productService).saveOrUpdate(boundProduct.capture());

        assertEquals(id, boundProduct.getValue().getId());
        assertEquals(description, boundProduct.getValue().getDescription());
        //assertEquals(price, boundProduct.getValue().getPrice());
        assertEquals(imageURL, boundProduct.getValue().getImageURL());
    }

    @Test
    public void testDelete() throws Exception {
        Integer id = 1;

        mockMvc.perform(get("/product/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/product/list"));

        verify(productService, times(1)).delete(id);
    }
}
