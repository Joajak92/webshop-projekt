package se.iths.joakim.webshopprojekt.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import se.iths.joakim.webshopprojekt.model.Product;
import se.iths.joakim.webshopprojekt.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductServiceH2Test {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
    }

    private Product createProduct(String name, String category) {
        Product product = new Product();
        product.setName(name);
        product.setCategory(category);
        product.setPrice(BigDecimal.valueOf(100));
        product.setImageUrl("https://placehold.co/300x300");
        return product;
    }

    @Test
    void findAll_shouldReturnProductsGroupedByCategory() {
        productRepository.save(createProduct("iPhone 17", "Mobil"));
        productRepository.save(createProduct("iPad", "Surfplatta"));

        Map<String, List<Product>> result = productService.findAll();

        assertEquals(2, result.size());
        assertTrue(result.containsKey("Mobil"));
        assertTrue(result.containsKey("Surfplatta"));
    }

    @Test
    void save_shouldSaveProductToDatabase() {
        Product product = createProduct("iPhone 17", "Mobil");

        productService.save(product);

        assertEquals(1, productRepository.findAll().size());
    }

    @Test
    void findById_shouldReturnProduct() {
        Product saved = productRepository.save(createProduct("iPhone 17", "Mobil"));

        Product result = productService.findById(saved.getId());

        assertNotNull(result);
        assertEquals("iPhone 17", result.getName());
    }

    @Test
    void findById_shouldThrowIfNotFound() {
        assertThrows(NoSuchElementException.class, () -> productService.findById(999L));
    }
}