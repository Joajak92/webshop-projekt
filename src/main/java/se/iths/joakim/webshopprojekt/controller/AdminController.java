package se.iths.joakim.webshopprojekt.controller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import se.iths.joakim.webshopprojekt.model.Product;
import se.iths.joakim.webshopprojekt.repository.ProductRepository;
import se.iths.joakim.webshopprojekt.validation.ProductValidator;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ProductRepository productRepository;

    public AdminController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping
    public String showAdminPage(Model model) {
        model.addAttribute("product", new Product());
        return "admin";
    }

    @PostMapping("/products")
    public String createProduct(@Valid Product product, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "admin";
        }

        ProductValidator validator = new ProductValidator();
        try {
            validator.validate(product);
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "admin";
        }

        productRepository.save(product);
        model.addAttribute("product", new Product());
        model.addAttribute("success", "Produkten skapades!");
        return "admin";
    }
}