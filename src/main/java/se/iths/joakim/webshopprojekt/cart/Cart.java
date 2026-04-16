package se.iths.joakim.webshopprojekt.cart;

import se.iths.joakim.webshopprojekt.validation.CartItemValidator;

import java.util.ArrayList;
import java.util.List;

public class Cart {

    private List<CartItem> items = new ArrayList<>();

    public void addItem(CartItem item) {

        CartItemValidator validator = new CartItemValidator();
        validator.validate(item);
        
        for (CartItem existing : items) {
            if (existing.getProductId() != null && existing.getProductId().equals(item.getProductId())) {
                existing.setQuantity(existing.getQuantity() + item.getQuantity());
                return;
            }
        }
        items.add(item);
    }

    public List<CartItem> getItems() {
        return items;
    }

    public double getTotalPrice() {
        return items.stream().mapToDouble(i -> i.getPrice() * i.getQuantity()).sum();
    }

    public void clear() {
        items.clear();
    }
}