package se.iths.joakim.webshopprojekt.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.ott.OneTimeToken;
import org.springframework.security.web.authentication.ott.OneTimeTokenGenerationSuccessHandler;
import org.springframework.security.web.authentication.ott.RedirectOneTimeTokenGenerationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import se.iths.joakim.springmessenger.model.Email;
import se.iths.joakim.springmessenger.service.MessageService;
import se.iths.joakim.springmessenger.service.OneTimeTokenService;

import java.io.IOException;

@Component
public class OttSuccessHandler implements OneTimeTokenGenerationSuccessHandler {

    private final RedirectOneTimeTokenGenerationSuccessHandler redirectHandler =
            new RedirectOneTimeTokenGenerationSuccessHandler("/ott/sent");
    private final OneTimeTokenService oneTimeTokenService;

    public OttSuccessHandler(OneTimeTokenService oneTimeTokenService) {
        this.oneTimeTokenService = oneTimeTokenService;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, OneTimeToken oneTimeToken) throws IOException, ServletException {
        System.out.println("=== HANDLE ANROPAS ===");

        String link = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/ott/verify")
                .queryParam("token", oneTimeToken.getTokenValue())
                .toUriString();

        try {
            oneTimeTokenService.sendVerificationEmail(
                    request.getParameter("username"),
                    oneTimeToken.getTokenValue(),
                    link
            );
            System.out.println("Mejl skickat!");
        } catch (Exception e) {
            System.out.println("FEL: " + e.getMessage());
            e.printStackTrace();
        }

        redirectHandler.handle(request, response, oneTimeToken);
    }
}
