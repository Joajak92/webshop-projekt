package se.iths.joakim.webshopprojekt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.iths.joakim.webshopprojekt.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
