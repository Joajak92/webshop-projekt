package se.iths.joakim.webshopprojekt.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import se.iths.joakim.webshopprojekt.model.OrderItem;
import se.iths.joakim.webshopprojekt.repository.OrderItemRepository;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OrderItemServiceH2Test {

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @BeforeEach
    void setUp() {
        orderItemRepository.deleteAll();
    }

    @Test
    void getAllOrderItems_shouldReturnAllItems() {
        OrderItem item1 = new OrderItem();
        item1.setProductName("iPhone 17");
        item1.setPrice(25000.0);
        item1.setQuantity(1);
        orderItemRepository.save(item1);

        OrderItem item2 = new OrderItem();
        item2.setProductName("iPad");
        item2.setPrice(10000.0);
        item2.setQuantity(2);
        orderItemRepository.save(item2);

        List<OrderItem> result = orderItemService.getAllOrderItems();

        assertEquals(2, result.size());
    }

    @Test
    void getOrderItemById_shouldReturnItem() {
        OrderItem item = new OrderItem();
        item.setProductName("iPhone 17");
        item.setPrice(25000.0);
        item.setQuantity(1);
        OrderItem saved = orderItemRepository.save(item);

        OrderItem result = orderItemService.getOrderItemById(saved.getId());

        assertNotNull(result);
        assertEquals("iPhone 17", result.getProductName());
    }

    @Test
    void getOrderItemById_shouldThrowIfNotFound() {
        assertThrows(NoSuchElementException.class, () -> orderItemService.getOrderItemById(999L));
    }
}