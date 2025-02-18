package io.github.kokila.inventory.manager.repository;

import io.github.kokila.inventory.manager.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository  extends JpaRepository<Product,Long> {

    Page<Product> findAll(Pageable pageable);
    List<Product> findByProductNameIgnoreCase(String productName);
}
