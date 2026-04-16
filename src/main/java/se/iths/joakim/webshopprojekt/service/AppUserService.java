package se.iths.joakim.webshopprojekt.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import se.iths.joakim.webshopprojekt.model.AppUser;
import se.iths.joakim.webshopprojekt.repository.AppUserRepository;
import se.iths.joakim.webshopprojekt.validation.AppUserValidator;

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

        AppUserValidator validator = new AppUserValidator();
        validator.validate(appUser);

        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));


        return appUserRepository.save(appUser);
    }
}
