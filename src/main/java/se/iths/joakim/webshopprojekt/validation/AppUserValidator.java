package se.iths.joakim.webshopprojekt.validation;

import se.iths.joakim.webshopprojekt.model.AppUser;

public class AppUserValidator {
    public void validate(AppUser user) {

        if (!user.isConsent()) {
            throw new IllegalArgumentException("Du måste godkänna integritetspolicyn.");
        }

        if (user.getPassword() == null || user.getPassword().length() < 8) {
            throw new IllegalArgumentException("Lösenordet måste vara minst 8 tecken långt");
        }

        if (user.getRole() == null || user.getRole().isBlank()) {
            user.setRole("USER");
        }

        if (!"USER".equals(user.getRole()) && !"ADMIN".equals(user.getRole())) {
            throw new IllegalArgumentException("Ogiltig roll.");
        }
    }
}
