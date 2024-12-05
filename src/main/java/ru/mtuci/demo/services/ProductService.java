package ru.mtuci.demo.services;


import ru.mtuci.demo.model.Products;

public interface ProductService {
    Products getProductById(Long id);
}

