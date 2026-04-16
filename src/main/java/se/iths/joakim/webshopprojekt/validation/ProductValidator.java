package se.iths.joakim.webshopprojekt.validation;

import se.iths.joakim.webshopprojekt.model.Product;

import java.math.BigDecimal;

public class ProductValidator {
    public void validate(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Produkt saknas.");
        }

        if (product.getName() == null || product.getName().isBlank()) {
            throw new IllegalArgumentException("Produkt måste ha ett namn.");
        }

        if (product.getCategory() == null || product.getCategory().isBlank()) {
            throw new IllegalArgumentException("Produkt måste ha en kategori.");
        }

        if (product.getImageUrl() == null || product.getImageUrl().isBlank()) {
            throw new IllegalArgumentException("Produkt måste ha en bild.");
        }

        if (product.getPrice() == null) {
            throw new IllegalArgumentException("Produkt måste ha ett pris.");
        }

        if (product.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Produktens pris måste vara större än 0.");
        }
    }
}
