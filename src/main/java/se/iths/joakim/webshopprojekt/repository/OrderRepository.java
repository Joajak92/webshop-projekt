package se.iths.joakim.webshopprojekt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.iths.joakim.webshopprojekt.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
