package se.iths.joakim.webshopprojekt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import se.iths.joakim.webshopprojekt.model.AppUser;
import se.iths.joakim.webshopprojekt.repository.AppUserRepository;

import java.security.Principal;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final AppUserRepository appUserRepository;

    public ProfileController(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @GetMapping
    public String showProfile(Principal principal, Model model) {
        AppUser user = appUserRepository.findByUsername(principal.getName())
                .orElseThrow();
        model.addAttribute("user", user);
        return "profile";
    }

    @PostMapping("/delete")
    public String deleteAccount(Principal principal) {
        AppUser user = appUserRepository.findByUsername(principal.getName())
                .orElseThrow();
        appUserRepository.delete(user);
        return "redirect:/logout";
    }

    @PostMapping("/send-data")
    public String sendUserData(Principal principal, Model model) {
        // emailService ska anropas här mvh kevin
        model.addAttribute("message", "Dina uppgifter har skickats till din email.");
        AppUser user = appUserRepository.findByUsername(principal.getName())
                .orElseThrow();
        model.addAttribute("user", user);
        return "profile";
    }
}