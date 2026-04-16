package se.iths.joakim.webshopprojekt.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.iths.joakim.webshopprojekt.model.OrderItem;
import se.iths.joakim.webshopprojekt.repository.OrderItemRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderItemServiceTest {

    @Mock
    private OrderItemRepository orderItemRepository;

    @InjectMocks
    private OrderItemService orderItemService;

    @Test
    void getAllOrderItems_shouldReturnAllItems() {
        OrderItem item1 = new OrderItem();
        OrderItem item2 = new OrderItem();
        when(orderItemRepository.findAll()).thenReturn(List.of(item1, item2));

        List<OrderItem> result = orderItemService.getAllOrderItems();

        assertEquals(2, result.size());
        verify(orderItemRepository, times(1)).findAll();
    }

    @Test
    void getOrderItemById_shouldReturnItem() {
        OrderItem item = new OrderItem();
        item.setId(1L);
        when(orderItemRepository.findById(1L)).thenReturn(Optional.of(item));

        OrderItem result = orderItemService.getOrderItemById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void getOrderItemById_shouldThrowIfNotFound() {
        when(orderItemRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> orderItemService.getOrderItemById(99L));
    }
}