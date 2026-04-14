package se.iths.joakim.webshopprojekt.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import se.iths.joakim.webshopprojekt.model.AppUser;
import se.iths.joakim.webshopprojekt.repository.AppUserRepository;

@Service
public class AppUserService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    public AppUserService(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AppUser register(AppUser appUser) {
        if (appUserRepository.findByUsername(appUser.getUsername()).isPresent()) {
            throw new IllegalArgumentException("E-postadressen används redan.");
        }
        if (!appUser.isConsent()) {
            throw new IllegalArgumentException("Du måste godkänna integritetspolicyn.");
        }

        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
        if (appUser.getRole() == null || appUser.getRole().isBlank()) {
            appUser.setRole("USER");
        }

        return appUserRepository.save(appUser);
    }
}
