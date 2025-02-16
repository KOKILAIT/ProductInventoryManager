package io.github.kokila.inventory.manager.integration.test;

import io.github.kokila.inventory.manager.entity.Product;
import io.github.kokila.inventory.manager.service.ProductService;
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
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ProductService productService;

    @Test
    public void testCreateProduct() {
        Product product = new Product();
        product.setProductName("pen");
        product.setProductDescription("writer");
        product.setProductPrice(20.0);
        product.setProductCategory("stationary");
        product.setProductQuantity(100);

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Product> entity = new HttpEntity<>(product, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/products"),
                HttpMethod.POST, entity, String.class);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("writer", productService.getProductsByName(product.getProductName()).get(0).getProductDescription());
    }
    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}
