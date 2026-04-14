package se.iths.joakim.webshopprojekt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.iths.joakim.webshopprojekt.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
