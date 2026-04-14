package se.iths.joakim.webshopprojekt.service;

import org.springframework.stereotype.Service;
import se.iths.joakim.webshopprojekt.model.OrderItem;
import se.iths.joakim.webshopprojekt.repository.OrderItemRepository;
import java.util.List;

@Service
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;

    public OrderItemService(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    public List<OrderItem> getAllOrderItems() {
        return orderItemRepository.findAll();
    }

    public OrderItem getOrderItemById(Long id) {
        return orderItemRepository.findById(id).orElseThrow();
    }
}