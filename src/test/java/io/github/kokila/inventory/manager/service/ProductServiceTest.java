package io.github.kokila.inventory.manager.service;

import io.github.kokila.inventory.manager.entity.Product;
import io.github.kokila.inventory.manager.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    Product firstPrduct= new Product();
    @InjectMocks
    private ProductService productService;
    
    @Mock
    private ProductRepository productRepository;

    @BeforeEach
    void productSetUp()
    {
        firstPrduct.setProductName("pen");
        firstPrduct.setProductDescription("writer");
        firstPrduct.setProductPrice(20.0);
        firstPrduct.setProductCategory("stationary");
        firstPrduct.setProductQuantity(100);
    }
    
    @Test
    void getProducts_productAvailableInDB_returnAllProducts(){

        Product secondProduct= new Product();
        secondProduct.setProductName("laptop");
        secondProduct.setProductDescription("electronics");
        secondProduct.setProductPrice(1500.0);
        secondProduct.setProductCategory("electronics");
        secondProduct.setProductQuantity(10);

        List<Product> list = List.of(firstPrduct,secondProduct);
        when(productRepository.findAll()).thenReturn(list);
        List<Product> products = productService.getProducts();
        assertEquals(list.size(),products.size());

    }
    @Test
    void getProducts_productNotAvailableInDB_returnAllProducts(){

        Product secondProduct= new Product();
        secondProduct.setProductName("laptop");
        secondProduct.setProductDescription("electronics");
        secondProduct.setProductPrice(1500.0);
        secondProduct.setProductCategory("electronics");
        secondProduct.setProductQuantity(10);

        List<Product> list = List.of(firstPrduct,secondProduct);
        when(productRepository.findAll()).thenReturn(List.of());
        List<Product> products = productService.getProducts();
        assertEquals(0,products.size());
    }

    @Test
    void getProductById_matchingProductAvailableInDB_returnThatProduct()
    {
        when(productRepository.findById(firstPrduct.getProductID())).thenReturn(Optional.of(firstPrduct));
        Product productById = productService.getProductById(firstPrduct.getProductID());
        assertEquals(firstPrduct,productById);
    }

    @Test
    void getProductById_matchingProductNotAvailableInDB_returnThatProduct()
    {
        when(productRepository.findById(firstPrduct.getProductID())).thenReturn(Optional.empty());
        Product productById = productService.getProductById(firstPrduct.getProductID());
        assertNull(productById);
    }

    @Test
    void createProduct_PassingValidProduct_SuccessfullSave()
    {
        firstPrduct.setProductID(1045L);
        when(productRepository.save(firstPrduct)).thenReturn(firstPrduct);
        Product product = productService.createProduct(firstPrduct);
        assertEquals(firstPrduct.getProductID(),product.getProductID());
    }

    @Test
    void createProduct_repoThrowingException_throwsException()
    {
        doThrow(new RuntimeException("exception occured while saving")).when(productRepository).save(firstPrduct);
        assertThrows(RuntimeException.class,() -> productService.createProduct(firstPrduct));
        verify(productRepository).save(firstPrduct);
    }

    @Test
    void updateProduct_passingAvailableProductId_returnThatProduct() throws Exception
    {
        firstPrduct.setProductID(1045L);
        when(productRepository.findById(firstPrduct.getProductID())).thenReturn(Optional.of(firstPrduct));
        String result = productService.updateProduct(firstPrduct, firstPrduct.getProductID());
        assertEquals("Updated Product successfully", result);
    }

    @Test
    void updateProduct_passingUnAvailableProductId_throwsException()
    {
        firstPrduct.setProductID(1045L);
        when(productRepository.findById(firstPrduct.getProductID())).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class,()->productService.updateProduct(firstPrduct,firstPrduct.getProductID()));
    }

    @Test
    void deleteProduct_passingAvailableProductId_thenReturnSuccessMsg() throws Exception {
        firstPrduct.setProductID(1045L);
        when(productRepository.findById(firstPrduct.getProductID())).thenReturn(Optional.of(firstPrduct));
        String result = productService.deleteProduct(firstPrduct.getProductID());
        assertEquals("Deleted successfully",result);
        verify(productRepository).deleteById(firstPrduct.getProductID());
    }

    @Test
    void deleteProduct_passingUnAvailableProductId_throwsException()
    {
        firstPrduct.setProductID(1045L);
        when(productRepository.findById(firstPrduct.getProductID())).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class,()->productService.deleteProduct(firstPrduct.getProductID()));
    }

}
