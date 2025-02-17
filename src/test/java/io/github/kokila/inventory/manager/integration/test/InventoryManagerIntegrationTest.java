package io.github.kokila.inventory.manager.integration.test;

import io.github.kokila.inventory.manager.entity.Product;
import io.github.kokila.inventory.manager.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class InventoryManagerIntegrationTest {
    Product product = new Product();
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ProductService productService;

    @BeforeEach
    void setUp()
    {
        product.setProductName("pen");
        product.setProductDescription("writer");
        product.setProductPrice(20.0);
        product.setProductCategory("stationary");
        product.setProductQuantity(100);
    }
    @Test
    public void testCreateProduct() {

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Product> entity = new HttpEntity<>(product, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/products"),
                HttpMethod.POST, entity, String.class);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("writer", productService.getProductsByName(product.getProductName()).get(0).getProductDescription());
    }
    @Test
    public void testUpdateProduct()
    {
        product.setProductQuantity(50);
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Product> entity= new HttpEntity<>(product,headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/products/402"),
                HttpMethod.PUT,entity,String.class);
        assertEquals(200,response.getStatusCodeValue());
        assertEquals(402,productService.getProductById(402L).getProductID());
        assertEquals(50,productService.getProductById(402L).getProductQuantity());
    }
    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}
