package se.iths.joakim.webshopprojekt.service;
import org.springframework.stereotype.Service;
import se.iths.joakim.webshopprojekt.cart.Cart;
import se.iths.joakim.webshopprojekt.cart.CartItem;
import se.iths.joakim.webshopprojekt.model.Order;
import se.iths.joakim.webshopprojekt.model.OrderItem;
import se.iths.joakim.webshopprojekt.repository.OrderRepository;
import java.time.LocalDate;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order createOrder(Cart cart, String username) {
        Order order = new Order();
        order.setUsername(username);
        order.setOrderDate(LocalDate.now());

        for (CartItem item : cart.getItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProductName(item.getProductName());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setPrice(item.getPrice());
            order.getOrderItems().add(orderItem);
        }

        order.setTotalPrice(cart.getTotalPrice());
        return orderRepository.save(order);
    }
}