package com.kasir.service;

import com.kasir.model.Product;
import com.kasir.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    
    @Autowired
    private ProductRepository productRepository;

    @PostConstruct
    public void initData() {
        // Data produk dummy jika database kosong
        if (productRepository.count() == 0) {
            productRepository.save(new Product("P001", "Nasi Goreng", 15000, 100));
            productRepository.save(new Product("P002", "Mie Goreng", 12000, 100));
            productRepository.save(new Product("P003", "Es Teh", 5000, 100));
            productRepository.save(new Product("P004", "Es Jeruk", 7000, 100));
            productRepository.save(new Product("P005", "Ayam Goreng", 20000, 50));
            productRepository.save(new Product("P006", "Sate Ayam", 18000, 50));
        }
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(String id) {
        return productRepository.findById(id);
    }

    public void updateStock(String productId, int quantity) {
        getProductById(productId).ifPresent(product -> {
            product.setStock(product.getStock() - quantity);
            productRepository.save(product);
        });
    }

    public void addProduct(Product product) {
        productRepository.save(product);
    }

    public void updateProduct(String id, String name, double price, int stock) {
        getProductById(id).ifPresent(product -> {
            product.setName(name);
            product.setPrice(price);
            product.setStock(stock);
            productRepository.save(product);
        });
    }

    public void deleteProduct(String productId) {
        productRepository.deleteById(productId);
    }
}
