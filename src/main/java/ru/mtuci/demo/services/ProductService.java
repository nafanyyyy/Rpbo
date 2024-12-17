package ru.mtuci.demo.services;


import ru.mtuci.demo.Request.ProductRequest;
import ru.mtuci.demo.model.Products;

public interface ProductService {
    Products getProductById(Long id);
    Products createProduct(ProductRequest request);
}

