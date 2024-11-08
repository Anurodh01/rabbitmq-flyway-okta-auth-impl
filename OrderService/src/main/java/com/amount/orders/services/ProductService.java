package com.amount.orders.services;

import com.amount.orders.models.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<Product> createProducts(List<Product> products);
    Product getProductById(int id);
    Product updateProductCount(int id, int quantity);
    void deleteAllProducts();
}
