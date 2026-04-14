package se.iths.joakim.webshopprojekt.controller;
import se.iths.joakim.webshopprojekt.cart.Cart;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import se.iths.joakim.webshopprojekt.model.Order;
import se.iths.joakim.webshopprojekt.service.OrderService;

import java.security.Principal;

@Controller
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/checkout")
    public String checkout(@SessionAttribute("cart") Cart cart,
                           Principal principal,
                           Model model) {
        Order order = orderService.createOrder(cart, principal.getName());
        model.addAttribute("order", order);
        return "order-confirmation";
    }
}
