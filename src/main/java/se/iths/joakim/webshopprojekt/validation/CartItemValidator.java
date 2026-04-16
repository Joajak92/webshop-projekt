package se.iths.joakim.webshopprojekt.validation;

import se.iths.joakim.webshopprojekt.cart.CartItem;

public class CartItemValidator {
    public void validate(CartItem item) {
        if (item.getProductId() == null) {
            throw new IllegalArgumentException("Produkt saknar ID.");
        }

        if (item.getQuantity() <= 0) {
            throw new IllegalArgumentException("Antal måste vara större än 0.");
        }

        if (item.getPrice() <= 0) {
            throw new IllegalArgumentException("Pris måste vara större än 0.");
        }

        if (item.getProductName() == null || item.getProductName().isBlank()) {
            throw new IllegalArgumentException("Produkt måste ha ett namn.");
        }
    }
}
