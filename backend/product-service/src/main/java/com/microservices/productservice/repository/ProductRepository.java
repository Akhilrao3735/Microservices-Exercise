package com.microservices.productservice.repository;

import com.microservices.productservice.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    Page<Product> findAll(Pageable pageable);

    @Query(value = "SELECT * FROM products WHERE price > :price", nativeQuery = true)
    List<Product> findProductsAbovePrice(@Param("price") Double price);
}