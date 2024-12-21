package ru.mtuci.demo.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.mtuci.demo.Request.ProductRequest;
import ru.mtuci.demo.Response.ProductResponse;
import ru.mtuci.demo.exception.ProductException;
import ru.mtuci.demo.model.Products;
import ru.mtuci.demo.repo.ProductRepository;
import ru.mtuci.demo.services.ProductService;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    @Override
    public Products createProduct(ProductRequest request) {
        if (productRepository.existsByName(request.getName())) {
            throw new ProductException("Продукт с именем '" + request.getName() + "' уже существует.");
        }

        Products product = new Products();
        product.setName(request.getName());
        product.setIsBlocked(request.getIsBlocked());
        return productRepository.save(product);
    }
    @Override
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Продукт с ID " + id + " не найден");
        }
        productRepository.deleteById(id);
    }
    @Override
    public List<ProductResponse> getAllProducts() {
        List<Products> products = productRepository.findAll();
        return products.stream()
                .map(product -> new ProductResponse(
                        product.getId(),
                        product.getName(),
                        product.getIsBlocked()
                ))
                .toList();
    }
    @Override
    public Products getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new RuntimeException("Продукт не найден."));
    }
}
