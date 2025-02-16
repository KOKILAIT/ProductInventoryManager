package io.github.kokila.inventory.manager.util;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;

import io.github.kokila.inventory.manager.entity.Product;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;

public class ProductUtilTest {



    @Test
    void copyProductProperties_validInput_copyValuesFromSourceToDestination() throws Exception {
        Product productFromRequest= new Product();
        Product productFromDB= new Product();
        productFromRequest.setProductName("pen");
        productFromRequest.setProductDescription("writer");
        productFromRequest.setProductPrice(20.0);
        productFromRequest.setProductCategory("stationary");
        productFromRequest.setProductQuantity(100);

        ProductUtil.copyProductProperties(productFromRequest,productFromDB);
        assertEquals(productFromRequest.getProductName(),productFromDB.getProductName());
        assertEquals(productFromRequest.getProductPrice(),productFromDB.getProductPrice());
        assertEquals(productFromRequest.getProductQuantity(),productFromDB.getProductQuantity());
        assertEquals(productFromRequest.getProductCategory(),productFromDB.getProductCategory());
        assertEquals(productFromRequest.getProductDescription(),productFromDB.getProductDescription());
    }

    @Test
    void copyProductProperties_partialInput_copyPartialInput() throws Exception{
        Product productFromRequest= new Product();
        Product productFromDB= new Product();

        productFromDB.setProductName("pen");
        productFromDB.setProductDescription("writer");
        productFromDB.setProductPrice(20.0);
        productFromDB.setProductCategory("stationary");
        productFromDB.setProductQuantity(100);

        productFromRequest.setProductName("pencil");
        productFromRequest.setProductDescription("temp writer");

        ProductUtil.copyProductProperties(productFromRequest,productFromDB);
        assertEquals(productFromRequest.getProductName(),productFromDB.getProductName());
        assertEquals(productFromRequest.getProductDescription(),productFromDB.getProductDescription());
        assertNotNull(productFromDB.getProductCategory());
    }

    @Test
    void copyProperties_throwBeanException()
    {
        Product productFromRequest= new Product();
        Product productFromDB = new Product();
        try(MockedStatic<BeanUtils> mockedBeanUtils = mockStatic(BeanUtils.class)){
        mockedBeanUtils.when(()->BeanUtils.copyProperties(any(Product.class), any(Product.class), any(String[].class)))
                .thenThrow(new BeansException("Exception occured while copying properties") {});

        assertThrows(RuntimeException.class,( )->{
        ProductUtil.copyProductProperties(productFromRequest,productFromDB);
        });
    }
    }
}
