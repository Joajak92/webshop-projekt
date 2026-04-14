package se.iths.joakim.webshopprojekt.service;

import org.springframework.stereotype.Service;
import se.iths.joakim.webshopprojekt.model.Product;
import se.iths.joakim.webshopprojekt.repository.ProductRepository;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Map<String, List<Product>> findAll() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .collect(Collectors.groupingBy(
                        Product::getCategory,
                        LinkedHashMap::new,
                        Collectors.toList()
                ));
    }

    public Product save(Product product) {
        return productRepository.save(product);
    }

    public Product findById(Long id) {
        return productRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Produkten med id: " + id + " kunde inte hittas")
        );
    }
}
