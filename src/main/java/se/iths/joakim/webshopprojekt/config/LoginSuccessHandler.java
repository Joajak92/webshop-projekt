package se.iths.joakim.webshopprojekt.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import se.iths.joakim.springmessenger.service.OneTimeTokenService;
import se.iths.joakim.webshopprojekt.repository.AppUserRepository;

import java.io.IOException;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final AppUserRepository appUserRepository;
    private final OneTimeTokenService oneTimeTokenService;

    public LoginSuccessHandler(AppUserRepository appUserRepository, OneTimeTokenService oneTimeTokenService) {
        this.appUserRepository = appUserRepository;
        this.oneTimeTokenService = oneTimeTokenService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        String username = authentication.getName();

        appUserRepository.findByUsername(username).ifPresent(user -> {
            user.setVerified(false);
            appUserRepository.save(user);
        });


        SecurityContextHolder.clearContext();
        HttpSession session = request.getSession(false);
        if (session != null) session.invalidate();


        try {
            response.sendRedirect("/ott/generate-for?username=" + username);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}