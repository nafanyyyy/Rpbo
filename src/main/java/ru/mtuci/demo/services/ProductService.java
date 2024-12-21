package ru.mtuci.demo.services;


import ru.mtuci.demo.Request.ProductRequest;
import ru.mtuci.demo.Response.ProductResponse;
import ru.mtuci.demo.model.Products;

import java.util.List;

public interface ProductService {
    Products getProductById(Long id);
    Products createProduct(ProductRequest request);
    void deleteProduct(Long id);
    void blockProduct(Long id);
    void unblockProduct(Long id);
    List<ProductResponse> getAllProducts();
}

