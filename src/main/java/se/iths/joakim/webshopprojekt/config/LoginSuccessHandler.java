package se.iths.joakim.webshopprojekt.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import se.iths.joakim.webshopprojekt.repository.AppUserRepository;

import java.io.IOException;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final AppUserRepository appUserRepository;

    public LoginSuccessHandler(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        String username = authentication.getName();
        appUserRepository.findByUsername(username).ifPresent(user -> {
            if (!user.isVerified()) {
                try {
                    response.sendRedirect("/not-verified");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return;
            }
            try {
                response.sendRedirect("/products");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}