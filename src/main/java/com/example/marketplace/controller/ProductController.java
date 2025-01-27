package com.example.marketplace.controller;

import com.example.marketplace.model.Product;
import com.example.marketplace.service.ProductService;
import jakarta.transaction.Transactional;
import org.hibernate.StaleObjectStateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {

    @Autowired
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<Product> getAllProducts(){
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id){
        return productService.getProductById(id);
    }

    @Transactional
    @PostMapping
    public Product saveProduct(@RequestBody Product product){
        try {
            return productService.saveProduct(product);
        } catch (StaleObjectStateException e) {
            // Gérer l'exception en indiquant qu'une autre transaction a modifié l'entité
            throw new RuntimeException("Conflit de version, veuillez réessayer.");
        }
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }

    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Long id, @RequestBody Product p){
        Product product = productService.getProductById(id);
        product.setName(p.getName());
        product.setDescription(p.getDescription());
        product.setPrice(p.getPrice());
        return productService.saveProduct(product);
    }
}
