package com.amount.orders.services.impl;

import com.amount.orders.exception.ResourceNotFoundException;
import com.amount.orders.models.Product;
import com.amount.orders.repository.ProductRepository;
import com.amount.orders.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    @Autowired
    public ProductServiceImpl(ProductRepository productRepository){
        this.productRepository = productRepository;
    }
    @Override
    public List<Product> createProducts(List<Product> products) {
        return productRepository.saveAll(products);
    }

    @Override
    public Product getProductById(int id) {
        return productRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Product with Id: "+ id+" not found!"));
    }

    @Override
    public Product updateProductCount(int id, int quantity) {
        Optional<Product> existingProductOptional = productRepository.findById(id);
        if(!existingProductOptional.isPresent()){
           throw new ResourceNotFoundException("Product with Id: "+ id+" not found!");
        }
        Product existingProduct = existingProductOptional.get();
        existingProduct.setCount(existingProduct.getCount() + quantity);
        return productRepository.save(existingProduct);
    }

    @Override
    public void deleteAllProducts() {
        productRepository.deleteAll();
    }
}
