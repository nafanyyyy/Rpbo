package ru.mtuci.demo.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.mtuci.demo.Request.ProductRequest;
import ru.mtuci.demo.model.Products;
import ru.mtuci.demo.services.ProductService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<Products> createProduct(@RequestBody ProductRequest request) {
        Products product = productService.createProduct(request);
        return ResponseEntity.ok(product);
    }
}
