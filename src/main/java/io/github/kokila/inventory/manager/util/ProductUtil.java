package io.github.kokila.inventory.manager.util;

import io.github.kokila.inventory.manager.entity.Product;
import io.github.kokila.inventory.manager.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;

import java.util.ArrayList;
import java.util.List;

public class ProductUtil {
    private static final Logger log = LoggerFactory.getLogger(ProductUtil.class);

    // Private constructor to prevent instantiation
    private ProductUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    /**
     * @param productFromRequest product object from update request
     * @param productFromDB      product object from DB
     * @throws Exception possibly beansException or generic Exception
     */
    public static void copyProductProperties(Product productFromRequest, Product productFromDB) throws Exception {

        List<String> nullProperties = new ArrayList<>();
        nullProperties.add("productID");
        try {
            if (productFromRequest.getProductName() == null) {
                nullProperties.add("productName");
            }
            if (productFromRequest.getProductCategory() == null) {
                nullProperties.add("productCategory");
            }
            if (productFromRequest.getProductDescription() == null) {
                nullProperties.add("productDescription");
            }
            if (productFromRequest.getProductQuantity() == null) {
                nullProperties.add("productQuantity");
            }
            if (productFromRequest.getProductPrice() == null) {
                nullProperties.add("productPrice");
            }
            String[] stringArray = nullProperties.toArray(new String[0]);
            BeanUtils.copyProperties(productFromRequest, productFromDB, stringArray);
        } catch (BeansException beansException) {
            log.error("Bean exception occurred while copying properties" + beansException.getMessage());
            throw new RuntimeException(beansException.getMessage());
        } catch (Exception e) {
            log.error("Bean exception occurred while copying properties" + e.getMessage());
            throw new Exception(e.getMessage());
        }

    }
}
