package io.github.kokila.inventory.manager.controller;

import io.github.kokila.inventory.manager.entity.Product;
import io.github.kokila.inventory.manager.exception.ResourceNotFoundException;
import io.github.kokila.inventory.manager.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(ProductsController.class)
public class ProductsControllerTest {
    Product product = new Product();
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @BeforeEach
    void setUp() {
        product.setProductID(1045L);
        product.setProductName("pen");
        product.setProductDescription("writer");
        product.setProductPrice(20.0);
        product.setProductCategory("stationary");
        product.setProductQuantity(100);
    }

    @Test
    void getProducts_whenProductsExist_returnsProductList() throws Exception {
        List<Product> products = List.of(product);
        when(productService.getProducts()).thenReturn(products);
        mockMvc.perform(get("/products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[{'productName':'pen','productDescription':'writer','productPrice':20.0,'productCategory':'stationary','productQuantity':100}]"));
    }

    @Test
    void getProducts_whenNoProductsExist_throwsResourceNotFoundException() throws Exception {
        when(productService.getProducts()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void getProductById_withAvailabeProductId_ReturnThatProduct() throws Exception {


        when(productService.getProductById(product.getProductID())).thenReturn(product);
        mockMvc.perform(get("/products/1045")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{'productID':1045,'productName':'pen','productDescription':'writer','productPrice':20.0,'productCategory':'stationary','productQuantity':100}"));
    }

    @Test
    void getProductById_withUnAvailabeProductId_throwsResourceNotFoundException() throws Exception {
        when(productService.getProductById(product.getProductID())).thenThrow(new ResourceNotFoundException("Product mentioned is not found"));
        mockMvc.perform(get("/products/1045")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void getProductByName_whenProductsExist_returnProductList() throws Exception{
        List <Product> products = List.of(product);
        when(productService.getProductsByName("pen")).thenReturn(products);
        mockMvc.perform(get("/products/search?productName=pen").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[{'productName':'pen','productDescription':'writer','productPrice':20.0,'productCategory':'stationary','productQuantity':100}]"));
    }

    @Test
    void createProduct_PassingValidProduct_SuccessfullSave() throws Exception {
        when(productService.createProduct(any(Product.class))).thenReturn(product);
        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"productName\":\"pen\",\"productDescription\":\"writer\",\"productPrice\":20.0,\"productCategory\":\"stationary\",\"productQuantity\":100}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{'productID':1045,'productName':'pen','productDescription':'writer','productPrice':20.0,'productCategory':'stationary','productQuantity':100}"));
    }

    @Test
    void createProduct_PassingInValidProduct_throwsException() throws Exception {
        when(productService.createProduct(any(Product.class))).thenThrow(new RuntimeException("Exception occured on creation"));
        mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"productName\":\"pen\",\"productDescription\":\"writer\",\"productPrice\":20.0,\"productCategory\":\"stationary\",\"productQuantity\":100}"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void updateProduct_withAvailableProductId_returnThatProduct() throws Exception {
        when(productService.updateProduct(any(Product.class), any(Long.class))).thenReturn("Updated Product successfully");
        mockMvc.perform(put("/products/1045")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"productName\":\"pen\",\"productDescription\":\"writer\",\"productPrice\":20.0,\"productCategory\":\"stationary\",\"productQuantity\":100}"))
                .andExpect(status().isOk());
    }

    @Test
    void updateProduct_PassingInValidProduct_throwsException() throws Exception {
    when(productService.updateProduct(any(Product.class), any(Long.class))).thenThrow(new ResourceNotFoundException("Product with mentioned ID is not found"));
        mockMvc.perform(put("/products/1045")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"productName\":\"pen\",\"productDescription\":\"writer\",\"productPrice\":20.0,\"productCategory\":\"stationary\",\"productQuantity\":100}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteProduct_withAvailableProductID_returnSuccessMsg() throws Exception {
        when(productService.deleteProduct(product.getProductID())).thenReturn("Deleted successfully");
        mockMvc.perform(delete("/products/1045")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void  deleteProduct_withUnAvailableProductID_throwsException() throws Exception {
        when(productService.deleteProduct(product.getProductID())).thenThrow(new ResourceNotFoundException("Exception occured on deleting due to unavailable ID"));
        mockMvc.perform(delete("/product/1045")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}


