package se.iths.joakim.webshopprojekt.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.iths.joakim.springmessenger.service.MessageService;
import se.iths.joakim.webshopprojekt.cart.Cart;
import se.iths.joakim.webshopprojekt.cart.CartItem;
import se.iths.joakim.webshopprojekt.model.Order;
import se.iths.joakim.webshopprojekt.repository.OrderRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private MessageService messageService;

    @InjectMocks
    private OrderService orderService;

    private Cart cart;

    @BeforeEach
    void setUp() {
        cart = new Cart();
        CartItem item = new CartItem();
        item.setProductId(1L);
        item.setProductName("iPhone 17");
        item.setPrice(25000.0);
        item.setQuantity(2);
        cart.addItem(item);
    }

    @Test
    void createOrder_shouldSaveOrder() {
        Order savedOrder = new Order();
        savedOrder.setUsername("test@email.se");
        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);

        Order result = orderService.createOrder(cart, "test@email.se");

        assertNotNull(result);
        assertEquals("test@email.se", result.getUsername());
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void createOrder_shouldClearCartAfterOrder() {
        when(orderRepository.save(any(Order.class))).thenReturn(new Order());

        orderService.createOrder(cart, "test@email.se");

        assertTrue(cart.getItems().isEmpty());
    }

    @Test
    void createOrder_shouldSendConfirmationEmail() {
        when(orderRepository.save(any(Order.class))).thenReturn(new Order());

        orderService.createOrder(cart, "test@email.se");

        verify(messageService, times(1)).send(any());
    }
}