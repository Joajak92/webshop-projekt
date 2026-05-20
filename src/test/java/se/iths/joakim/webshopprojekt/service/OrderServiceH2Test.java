package se.iths.joakim.webshopprojekt.service;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import se.iths.joakim.springmessenger.service.MessageService;
import se.iths.joakim.webshopprojekt.cart.Cart;
import se.iths.joakim.webshopprojekt.cart.CartItem;
import se.iths.joakim.webshopprojekt.model.Order;
import se.iths.joakim.webshopprojekt.repository.OrderRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Transactional
@SpringBootTest
class OrderServiceH2Test {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @MockitoBean
    private MessageService messageService;

    private Cart cart;

    @BeforeEach
    void setUp() {
        orderRepository.deleteAll();
        cart = new Cart();
        CartItem item = new CartItem();
        item.setProductId(1L);
        item.setProductName("iPhone 17");
        item.setPrice(25000.0);
        item.setQuantity(2);
        cart.addItem(item);
    }

    @Test
    void createOrder_shouldSaveOrderToDatabase() {
        orderService.createOrder(cart, "test@email.se");

        assertEquals(1, orderRepository.findAll().size());
    }

    @Test
    void createOrder_shouldSaveCorrectUsername() {
        orderService.createOrder(cart, "test@email.se");

        Order saved = orderRepository.findAll().get(0);
        assertEquals("test@email.se", saved.getUsername());
    }

    @Test
    void createOrder_shouldSaveOrderItems() {
        orderService.createOrder(cart, "test@email.se");

        Order saved = orderRepository.findAll().get(0);
        assertEquals(1, saved.getOrderItems().size());
    }

    @Test
    void createOrder_shouldClearCartAfterOrder() {
        orderService.createOrder(cart, "test@email.se");

        assertTrue(cart.getItems().isEmpty());
    }

    @Test
    void createOrder_shouldSendConfirmationEmail() {
        orderService.createOrder(cart, "test@email.se");

        verify(messageService, times(1)).send(any());
    }
}