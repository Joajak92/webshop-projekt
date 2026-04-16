package se.iths.joakim.webshopprojekt.service;

import org.springframework.stereotype.Service;
import se.iths.joakim.springmessenger.model.Email;
import se.iths.joakim.springmessenger.service.MessageService;
import se.iths.joakim.webshopprojekt.cart.Cart;
import se.iths.joakim.webshopprojekt.cart.CartItem;
import se.iths.joakim.webshopprojekt.model.Order;
import se.iths.joakim.webshopprojekt.model.OrderItem;
import se.iths.joakim.webshopprojekt.repository.OrderRepository;

import java.time.LocalDate;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final MessageService messageService;

    public OrderService(OrderRepository orderRepository, MessageService messageService) {
        this.orderRepository = orderRepository;
        this.messageService = messageService;
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
        Order saved = orderRepository.save(order);
        cart.clear();

        sendOrderConfirmationEmail(saved, username);

        return saved;
    }

    private void sendOrderConfirmationEmail(Order order, String username) {
        StringBuilder sb = new StringBuilder();
        sb.append("Tack för din beställning!\n\n");
        sb.append("Datum: ").append(order.getOrderDate()).append("\n\n");
        sb.append("Produkter:\n");
        for (OrderItem item : order.getOrderItems()) {
            sb.append("- ").append(item.getProductName())
                    .append(" x").append(item.getQuantity())
                    .append(" = ").append(item.getPrice() * item.getQuantity()).append(" kr\n");
        }
        sb.append("\nTotalt: ").append(order.getTotalPrice()).append(" kr");

        Email email = new Email();
        email.setRecipient(username);
        email.setSubject("Orderbekräftelse");
        email.setMessage(sb.toString());
        messageService.send(email);
    }
}