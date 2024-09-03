package com.microservices.product.service.service;

import com.microservices.product.service.DTO.ProductRequest;
import com.microservices.product.service.DTO.ProductResponse;
import com.microservices.product.service.model.Product;
import com.microservices.product.service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;

    public void createProduct(ProductRequest productRequest) {
        Product product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .build();
        productRepository.save(product);
        log.info("Product{} created", product.getId());
    }

    public List<Product> getAllProducts() {
        List<Product> products = productRepository.findAll();
        products.stream().map(product -> ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build()).toList();
        return products;
    }
}
