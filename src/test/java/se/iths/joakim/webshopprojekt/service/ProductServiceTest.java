package se.iths.joakim.webshopprojekt.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.iths.joakim.webshopprojekt.model.Product;
import se.iths.joakim.webshopprojekt.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

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
        Product p1 = createProduct("iPhone 17", "Mobil");
        Product p2 = createProduct("iPad", "Surfplatta");
        when(productRepository.findAll()).thenReturn(List.of(p1, p2));

        Map<String, List<Product>> result = productService.findAll();

        assertEquals(2, result.size());
        assertTrue(result.containsKey("Mobil"));
        assertTrue(result.containsKey("Surfplatta"));
    }

    @Test
    void findAll_shouldGroupMultipleProductsInSameCategory() {
        Product p1 = createProduct("iPhone 17", "Mobil");
        Product p2 = createProduct("Samsung Galaxy", "Mobil");
        when(productRepository.findAll()).thenReturn(List.of(p1, p2));

        Map<String, List<Product>> result = productService.findAll();

        assertEquals(1, result.size());
        assertEquals(2, result.get("Mobil").size());
    }

    @Test
    void save_shouldSaveProduct() {
        Product product = createProduct("iPhone 17", "Mobil");
        when(productRepository.save(any())).thenReturn(product);

        Product result = productService.save(product);

        assertNotNull(result);
        verify(productRepository, times(1)).save(any());
    }

    @Test
    void findById_shouldReturnProduct() {
        Product product = createProduct("iPhone 17", "Mobil");
        product.setId(1L);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Product result = productService.findById(1L);

        assertNotNull(result);
        assertEquals("iPhone 17", result.getName());
    }

    @Test
    void findById_shouldThrowIfNotFound() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> productService.findById(99L));
    }
}