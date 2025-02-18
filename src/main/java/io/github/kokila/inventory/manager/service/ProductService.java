package io.github.kokila.inventory.manager.service;

import io.github.kokila.inventory.manager.entity.Product;
import io.github.kokila.inventory.manager.exception.ResourceNotFoundException;
import io.github.kokila.inventory.manager.repository.ProductRepository;
import io.github.kokila.inventory.manager.util.ProductUtil;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {
    private static final Logger log = LoggerFactory.getLogger(ProductService.class);
    @Autowired
    private ProductRepository productRepository;

    /**
     * @return list of all products available in DB or empty list if nothing is available in DB
     */
    @Cacheable("ListofAllProducts")
    public List<Product> getProducts() {
        List<Product> allProducts = new ArrayList<>();
        try {
            allProducts = productRepository.findAll(Sort.by("productCategory"));
        } catch (Exception e) {
            log.error("Exception occured while retrieving all products" + e.getMessage());
            throw new RuntimeException("Exception occured while retrieving all products");
        }
        return allProducts;
    }

    /**
     * @param page the page number to retrieve
     * @param size the number of products per page
     * @return a page of products
     */
    public Page<Product> getProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("productName"));
        return productRepository.findAll(pageable);
    }

    /**
     * @param id of the product to be fetched
     * @return product with matching ID of found or null object
     */
    public Product getProductById(Long id) {
        Product returendProduct = null;
        try {
            returendProduct = productRepository.findById(id).orElse(null);
        } catch (Exception e) {
            log.error("Exception occured while retrieving product by ID" + e.getMessage());
        }
        return returendProduct;
    }

    /**
     * @param product object to be inserted in DB
     * @return product with ID populated if it is success or exception
     */
    @Transactional
    public Product createProduct(Product product) {
        Product savedProduct = null;
        try {
            savedProduct = productRepository.save(product);
        } catch (Exception e) {
            throw new RuntimeException("Exception occurred while creating product" + e.getMessage());
        }
        return savedProduct;
    }

    /**
     * @param product bject to be updated
     * @param id      of the product to be updated
     * @return success message or exception
     */
    @Transactional
    public String updateProduct(Product product, Long id) throws Exception {
        Product productFromDB = productRepository.findById(id).orElse(null);
        if (productFromDB != null) {
            ProductUtil.copyProductProperties(product, productFromDB);
            productRepository.save(productFromDB);
            return "Updated Product successfully";
        } else {
            throw new ResourceNotFoundException("Product to be updates is not found");
        }
    }

    /**
     * @param id of the product to be deleted from DB
     * @return success message or exception
     */
    @Transactional
    public String deleteProduct(Long id) throws Exception {
        if (productRepository.findById(id).isPresent()) {
            productRepository.deleteById(id);
            log.debug("Successfully deleted productID: " + id);
            return "Deleted successfully";
        } else {
            log.warn("Trying to delete unavailable product ID: " + id);
            throw new ResourceNotFoundException("Trying to delete unavailable product ID: " + id);
        }
    }

    /**
     * @param productName to be fetched
     * @return list of matched products
     */
    public List<Product> getProductsByName(String productName) {
        return productRepository.findByProductNameIgnoreCase(productName);
    }
}
