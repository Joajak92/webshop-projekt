package se.iths.joakim.webshopprojekt.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import se.iths.joakim.webshopprojekt.model.AppUser;
import se.iths.joakim.webshopprojekt.repository.AppUserRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AppUserServiceH2Test {

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private AppUserRepository appUserRepository;

    @BeforeEach
    void setUp() {
        appUserRepository.deleteAll();
    }

    @Test
    void register_shouldSaveUserToDatabase() {
        AppUser user = new AppUser();
        user.setUsername("test@email.se");
        user.setPassword("password123");
        user.setConsent(true);

        appUserService.register(user);

        assertTrue(appUserRepository.findByUsername("test@email.se").isPresent());
    }

    @Test
    void register_shouldThrowIfEmailAlreadyExists() {
        AppUser user = new AppUser();
        user.setUsername("test@email.se");
        user.setPassword("password123");
        user.setConsent(true);
        appUserService.register(user);

        AppUser duplicate = new AppUser();
        duplicate.setUsername("test@email.se");
        duplicate.setPassword("password123");
        duplicate.setConsent(true);

        assertThrows(IllegalArgumentException.class, () -> appUserService.register(duplicate));
    }

    @Test
    void register_shouldEncodePassword() {
        AppUser user = new AppUser();
        user.setUsername("test@email.se");
        user.setPassword("password123");
        user.setConsent(true);

        appUserService.register(user);

        AppUser saved = appUserRepository.findByUsername("test@email.se").orElseThrow();
        assertNotEquals("password123", saved.getPassword());
    }

    @Test
    void register_shouldSetDefaultRole() {
        AppUser user = new AppUser();
        user.setUsername("test@email.se");
        user.setPassword("password123");
        user.setConsent(true);

        appUserService.register(user);

        AppUser saved = appUserRepository.findByUsername("test@email.se").orElseThrow();
        assertEquals("USER", saved.getRole());
    }
}