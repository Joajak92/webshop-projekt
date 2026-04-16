package se.iths.joakim.webshopprojekt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import se.iths.joakim.springmessenger.model.Email;
import se.iths.joakim.springmessenger.service.MessageService;
import se.iths.joakim.webshopprojekt.model.AppUser;
import se.iths.joakim.webshopprojekt.repository.AppUserRepository;

import java.security.Principal;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final AppUserRepository appUserRepository;
    private final MessageService messageService;

    public ProfileController(AppUserRepository appUserRepository, MessageService messageService) {
        this.appUserRepository = appUserRepository;
        this.messageService = messageService;
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
        AppUser user = appUserRepository.findByUsername(principal.getName())
                .orElseThrow();

        Email email = new Email();
        email.setRecipient(user.getUsername());
        email.setSubject("Dina uppgifter");
        email.setMessage("Email: " + user.getUsername() + "\nRoll: " + user.getRole());
        messageService.send(email);

        model.addAttribute("message", "Dina uppgifter har skickats till din email.");
        model.addAttribute("user", user);
        return "profile";
    }
}