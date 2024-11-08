package com.amount.orders.config;

import com.amount.orders.models.Product;
import com.amount.orders.services.ProductService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

//@Component    //comment so that this bean won't created and db won't be initialize with this way
@Slf4j
public class ProductDataInitializer {
    private List<Product> products;
    private ProductService productService;
    @Autowired
    private ProductDataInitializer(ProductService productService){
        this.productService = productService;
    }

    @PostConstruct
    public void insertProductData(){
        log.info("Initializing the Product DataBase..");
        products = Arrays.asList(
                new Product(1,"Phone",10,10000),
                new Product(2,"Laptop",6,70000),
                new Product(3,"Sunglasses",7,3000),
                new Product(4,"Camera",8,20000),
                new Product(5,"SmartWatch",3, 4000),
                new Product(6,"HeadPhone",4,8000),
                new Product(7,"Backpack",8,5000),
                new Product(8,"Shoes",9,8000),
                new Product(9,"Mouse",7,2000),
                new Product(10,"T-Shirt",9,6000)
        );
        productService.createProducts(products);
        log.info("Product Database initialized!");
    }

    @PreDestroy
    public void deleteProductData(){
        productService.deleteAllProducts();
        log.info("Product Database cleared!");
    }

}
