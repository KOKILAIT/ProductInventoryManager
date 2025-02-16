package io.github.kokila.inventory.manager.repository;

import io.github.kokila.inventory.manager.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository  extends JpaRepository<Product,Long> {
    List<Product> findByProductNameIgnoreCase(String productName);
}
