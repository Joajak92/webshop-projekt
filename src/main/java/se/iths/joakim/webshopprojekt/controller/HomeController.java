package se.iths.joakim.webshopprojekt.controller;




import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "index";
    }


    @GetMapping("/privacy-policy")
    public String privacyPolicy() {
        return "privacy-policy";
    }

    @GetMapping("/cookie-policy")
    public String cookiePolicy() {
        return "cookie-policy";
    }
    @GetMapping("/ott/sent")
    public String ottSent() {
        return "email-sent";
    }

    @GetMapping("/ott/verify")
    public String ottVerify(@RequestParam String token, Model model) {
        model.addAttribute("token", token);
        return "ott-verify";
    }
    @GetMapping("/not-verified")
    public String notVerified() {
        return "not-verified";
    }
    @GetMapping("/ott/generate-for")
    public String generateFor(@RequestParam String username, Model model) {
        model.addAttribute("username", username);
        return "generate-ott";
    }
}
