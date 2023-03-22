package com.example.springweb.service;

import com.example.springweb.exception.ProductNotFoundException;
import com.example.springweb.model.Product;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static java.util.List.copyOf;

@Service
public class ProductService {

    public static List<Product> products = new ArrayList<>();

    static {
        products.addAll(List.of(
                Product.builder().id(UUID.randomUUID()).name("Xbox Series S").description("Budget gaming device").createdAt(LocalDateTime.now()).price(299.99).build(),
                Product.builder().id(UUID.randomUUID()).name("Xbox Series X").description("Powerful gaming device").createdAt(LocalDateTime.now()).price(499.99).build(),
                Product.builder().id(UUID.randomUUID()).name("PS5").description("Powerful gaming device").createdAt(LocalDateTime.now()).price(500).build(),
                Product.builder().id(UUID.randomUUID()).name("Nintendo Switch OLED").description("Great but not powerful enough for most games").createdAt(LocalDateTime.now()).price(349.99).build()
        ));
    }

    public List<Product> getProducts(Optional<Double> maximalPrice, Optional<Double> minimalPrice) {
        AtomicReference<List<Product>> productsFiltered = new AtomicReference<>(copyOf(products));
        maximalPrice.ifPresent(max -> {
            productsFiltered.set(productsFiltered.get().stream().filter(product -> product.price() <= max).toList());
        });
        minimalPrice.ifPresent(min -> {
            productsFiltered.set(productsFiltered.get().stream().filter(product -> product.price() >= min).toList());
        });
        return productsFiltered.get();
    }

    public Product getProduct(UUID id) {
        return checkIfProductExistsAndReturn(id);
    }

    public UUID createProduct(Product product) {
        product.id(UUID.randomUUID());
        products.add(product);
        return product.id();
    }

    public Product updateProduct(UUID id, Product productToUpdate) {
        checkIfProductExistsAndReturn(id);
        deleteProduct(id);
        productToUpdate.id(id);
        products.add(productToUpdate);
        return productToUpdate;
    }

    public Product updateProductPrice(UUID id, double price) {
        var p = checkIfProductExistsAndReturn(id);
        products.remove(p);
        p.price(price);
        products.add(p);
        return p;
    }

    public void deleteProduct(UUID id) {
        Product product = checkIfProductExistsAndReturn(id);
        products.remove(product);
    }

    private Product checkIfProductExistsAndReturn(UUID id) {
        return products.stream().filter(product -> product.id().equals(id))
                .findFirst().orElseThrow(() -> new ProductNotFoundException(id));
    }
}
