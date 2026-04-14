package se.iths.joakim.webshopprojekt.controller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import se.iths.joakim.webshopprojekt.model.AppUser;
import se.iths.joakim.webshopprojekt.service.AppUserService;

@Controller
@RequestMapping("/register")
public class RegisterController {

    private final AppUserService appUserService;

    public RegisterController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @GetMapping
    public String showRegisterForm(Model model) {
        model.addAttribute("appUser", new AppUser());
        return "register";
    }

    @PostMapping
    public String register(@Valid AppUser appUser, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "register";
        }

        try {
            appUserService.register(appUser);
        } catch (IllegalArgumentException ex) {
            model.addAttribute("registerError", ex.getMessage());
            return "register";
        }

        model.addAttribute("username", appUser.getUsername());
        return "email-sent";
    }
}