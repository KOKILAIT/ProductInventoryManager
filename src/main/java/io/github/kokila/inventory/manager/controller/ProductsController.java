package io.github.kokila.inventory.manager.controller;

import io.github.kokila.inventory.manager.entity.Product;
import io.github.kokila.inventory.manager.exception.ResourceNotFoundException;
import io.github.kokila.inventory.manager.repository.ProductRepository;
import io.github.kokila.inventory.manager.service.ProductService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.ProducerTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/products")
public class ProductsController {
    private static final Logger log = LoggerFactory.getLogger(ProductsController.class);
    @Autowired
    private ProductService productService;

    @Autowired
    private ProducerTemplate producerTemplate;

    /**
     * @return list of products from database
     */
    @GetMapping
    public List<Product> getProducts() {
        try {
            List<Product> products = productService.getProducts();
            if (products.isEmpty()) {
                log.error("No products were found");
                throw new ResourceNotFoundException("No products were found");
            }
            log.info("Products successfully fetched");
            return products;
        } catch (ResourceNotFoundException ex) {
            log.error("Exception while fetching all products" + ex.getMessage());
            throw new ResourceNotFoundException("No products were found ");
        } catch (Exception e) {
            log.error("Exception while fetching all products" + e.getMessage());
            throw new RuntimeException("Exception while fetching all products" + e.getMessage());
        }
    }

    /**
     *
     * @param page the page number to retrieve
     * @param size the number of products per page
     * @return page of products
     */
    @GetMapping("/pages")
    public Page<Product> getProducts(@RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "5") int size) {
        return productService.getProducts(page, size);
    }
    /**
     * @param id input read from URI parameter
     * @return matching product details if available else throw resource not found exception
     */
    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        Product returnedProduct = productService.getProductById(id);
        if (returnedProduct == null) {
            throw new ResourceNotFoundException("Product with mentioned ID is not present");
        }
        return returnedProduct;
    }

    /**
     *
     * @param  product details
     * @return either success or failure message with HttpStatus code
     */
//    @PostMapping
//    public Object createProduct(@Valid @RequestBody  Product product) {
//        Product savedProduct = null;
//        try {
//            savedProduct = productService.createProduct(product);
//            log.info("Successfully created product");
//
//        } catch (Exception e) {
//            log.error("Product creation failed");
//            throw new RuntimeException(e.getMessage());
//        }
//        return savedProduct != null ? savedProduct : "Product creation failed";
//    }

    @PostMapping
    public String createProduct(@RequestBody Product product) {
        producerTemplate.sendBody("activemq:queue:productQueue", product);
        return "Product creation request has been sent to the queue";
    }

    /**
     *
     * @param  product details to be updated
     * @param id of the particular product to be updated
     * @return either success or failure message with HttpStatus code
     */
    @PutMapping("/{id}")
    public String updateProduct(@Valid @RequestBody Product product, @PathVariable Long id) {

        try {
            log.info("Product updated successfully");
            return productService.updateProduct(product, id);

        } catch (ResourceNotFoundException ex) {
            log.error("Product to be updates is not found" + ex.getMessage());
            throw new ResourceNotFoundException("Product to be updates is not found");
        } catch (Exception ex) {
            log.error("Exception occured in product update" + ex.getMessage());
            throw new RuntimeException("Exception occured in product update");
        }
    }

    /**
     *
     * @param id of the product to be deleted
     * @return either success or failure message with HttpStatus code
     */
    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable Long id) {
        try {
            return productService.deleteProduct(id);
        } catch (ResourceNotFoundException ex) {
            log.error("Trying to delete unavailable product ID: " + id);
            throw new ResourceNotFoundException(ex.getMessage());
        } catch (Exception ex) {
            log.error("Exception occured on deleting");
            throw new RuntimeException(ex.getMessage());
        }
    }

    /**
     *
     * @param productName that is  to fetched
     * @return list of matching products or failure message with HttpStatus code
     */
    @GetMapping("/search")
    public List<Product> getProductsByName(@RequestParam String productName) {
        List<Product> returnedProducts = productService.getProductsByName(productName);
        if (returnedProducts.isEmpty()) {
            log.error("No products were found");
            throw new ResourceNotFoundException("No products were found");
        }
        log.info("Products successfully fetched");
        return returnedProducts;
    }

}
