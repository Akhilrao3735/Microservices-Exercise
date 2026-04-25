package com.microservices.productservice.service;

import com.microservices.productservice.entity.Product;
import com.microservices.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import java.util.stream.Collectors;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    public Product createProduct(Product product) {
        log.info("Creating product: {}", product.getName());
        return productRepository.save(product);
    }

    public List<Product> getAllProducts() {
        log.info("Fetching all products");
        return productRepository.findAll();
    }

    public Product getProductById(Integer id) {
        log.info("Fetching product by id: {}", id);
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    public Product updateProduct(Integer id, Product product) {
        log.info("Updating product with id: {}", id);
        Product existing = getProductById(id);
        existing.setName(product.getName());
        existing.setPrice(product.getPrice());
        existing.setStock(product.getStock());
        return productRepository.save(existing);
    }

    public void deleteProduct(Integer id) {
        log.info("Deleting product with id: {}", id);
        productRepository.deleteById(id);
    }
    public Page<Product> getProductsWithPagination(int page, int size, String sortBy) {
        log.info("Fetching products with pagination - page: {}, size: {}, sortBy: {}", page, size, sortBy);
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return productRepository.findAll(pageable);
    }

    public List<Product> getProductsAbovePrice(Double price) {
        log.info("Fetching products above price: {}", price);
        return productRepository.findAll()
                .stream()
                .filter(p -> p.getPrice() > price)
                .collect(Collectors.toList());
    }
    public List<Product> getProductsByNativeQuery(Double price) {
        log.info("Fetching products above price using native query: {}", price);
        return productRepository.findProductsAbovePrice(price);
    }
}
