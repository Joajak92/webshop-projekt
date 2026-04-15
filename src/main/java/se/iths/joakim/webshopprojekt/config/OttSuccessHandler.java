package se.iths.joakim.webshopprojekt.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.ott.OneTimeToken;
import org.springframework.security.authentication.ott.OneTimeTokenService;
import org.springframework.security.web.authentication.ott.OneTimeTokenGenerationSuccessHandler;
import org.springframework.security.web.authentication.ott.RedirectOneTimeTokenGenerationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import se.iths.joakim.springmessenger.model.Email;
import se.iths.joakim.springmessenger.service.MessageService;


import java.io.IOException;

@Component
public class OttSuccessHandler implements OneTimeTokenGenerationSuccessHandler {

    private final MessageService messageService;
    private final OneTimeTokenService oneTimeTokenService;
    private final RedirectOneTimeTokenGenerationSuccessHandler redirectOneTimeTokenGenerationSuccessHandler;

    public OttSuccessHandler(MessageService messageService, OneTimeTokenService oneTimeTokenService, RedirectOneTimeTokenGenerationSuccessHandler redirectOneTimeTokenGenerationSuccessHandler) {
        this.messageService = messageService;
        this.oneTimeTokenService = oneTimeTokenService;
        this.redirectOneTimeTokenGenerationSuccessHandler = redirectOneTimeTokenGenerationSuccessHandler;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, OneTimeToken oneTimeToken) throws IOException, ServletException {
        System.out.println("OttSuccessHandler");
        String link = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/login/ott")
                .queryParam("token", oneTimeToken.getTokenValue())
                .toUriString();



        Email email = new Email();
        email.setRecipient(oneTimeToken.getUsername());
        email.setMessage(link);
        email.setSubject("One time token link");
        System.out.println("📨 SKICKAR EMAIL VIA MESSAGESERVICE");
        messageService.send(email);
        redirectOneTimeTokenGenerationSuccessHandler.handle(request, response, oneTimeToken);
    }
}
