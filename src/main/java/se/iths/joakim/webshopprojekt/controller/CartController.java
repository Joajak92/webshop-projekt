package se.iths.joakim.webshopprojekt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import se.iths.joakim.webshopprojekt.cart.Cart;
import se.iths.joakim.webshopprojekt.cart.CartItem;
import se.iths.joakim.webshopprojekt.model.Product;
import se.iths.joakim.webshopprojekt.repository.ProductRepository;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final ProductRepository productRepository;

    public CartController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping
    public String showCart(HttpSession session, Model model) {
        Cart cart = getOrCreateCart(session);
        model.addAttribute("cart", cart);
        return "cart";
    }

    @PostMapping("/add")
    public String addToCart(@RequestParam Long productId,
                            @RequestParam(defaultValue = "1") int quantity,
                            HttpSession session) {
        Cart cart = getOrCreateCart(session);

        Product product = productRepository.findById(productId)
                .orElseThrow();

        CartItem item = new CartItem();
        item.setProductId(product.getId());
        item.setProductName(product.getName());
        item.setPrice(product.getPrice().doubleValue());
        item.setQuantity(quantity);

        cart.addItem(item);
        session.setAttribute("cart", cart);

        return "redirect:/cart";
    }

    @PostMapping("/clear")
    public String clearCart(HttpSession session) {
        Cart cart = getOrCreateCart(session);
        cart.clear();
        session.setAttribute("cart", cart);
        return "redirect:/cart";
    }

    private Cart getOrCreateCart(HttpSession session) {
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
            session.setAttribute("cart", cart);
        }
        return cart;
    }
}