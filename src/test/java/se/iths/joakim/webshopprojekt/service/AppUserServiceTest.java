package se.iths.joakim.webshopprojekt.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import se.iths.joakim.webshopprojekt.model.AppUser;
import se.iths.joakim.webshopprojekt.repository.AppUserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppUserServiceTest {

    @Mock
    private AppUserRepository appUserRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AppUserService appUserService;

    private AppUser createValidUser() {
        AppUser user = new AppUser();
        user.setUsername("test@email.se");
        user.setPassword("password123");
        user.setConsent(true);
        return user;
    }

    @Test
    void register_shouldSaveUser() {
        AppUser user = createValidUser();
        when(appUserRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        when(appUserRepository.save(any())).thenReturn(user);

        AppUser result = appUserService.register(user);

        assertNotNull(result);
        verify(appUserRepository, times(1)).save(any());
    }

    @Test
    void register_shouldThrowIfEmailAlreadyExists() {
        AppUser user = createValidUser();
        when(appUserRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        assertThrows(IllegalArgumentException.class, () -> appUserService.register(user));
        verify(appUserRepository, never()).save(any());
    }

    @Test
    void register_shouldThrowIfConsentIsFalse() {
        AppUser user = createValidUser();
        user.setConsent(false);
        when(appUserRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> appUserService.register(user));
        verify(appUserRepository, never()).save(any());
    }

    @Test
    void register_shouldThrowIfPasswordTooShort() {
        AppUser user = createValidUser();
        user.setPassword("short");
        when(appUserRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> appUserService.register(user));
        verify(appUserRepository, never()).save(any());
    }

    @Test
    void register_shouldEncodePassword() {
        AppUser user = createValidUser();
        when(appUserRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        when(appUserRepository.save(any())).thenReturn(user);

        appUserService.register(user);

        verify(passwordEncoder, times(1)).encode(any());
    }

    @Test
    void register_shouldSetDefaultRoleIfNull() {
        AppUser user = createValidUser();
        when(appUserRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        when(appUserRepository.save(any())).thenReturn(user);

        appUserService.register(user);

        assertEquals("USER", user.getRole());
    }
}