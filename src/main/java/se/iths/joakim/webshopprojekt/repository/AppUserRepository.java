package se.iths.joakim.webshopprojekt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.iths.joakim.webshopprojekt.model.AppUser;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByUsername(String username);
}
