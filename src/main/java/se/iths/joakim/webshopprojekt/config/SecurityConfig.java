package se.iths.joakim.webshopprojekt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.security.authentication.ott.JdbcOneTimeTokenService;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import se.iths.joakim.webshopprojekt.repository.AppUserRepository;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final AppUserRepository appUserRepository;

    public SecurityConfig(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public org.springframework.security.authentication.ott.OneTimeTokenService jdbcOneTimeTokenService(JdbcOperations jdbcOperations) {
        return new JdbcOneTimeTokenService(jdbcOperations);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, OttSuccessHandler ottSuccessHandler, LoginSuccessHandler loginSuccessHandler) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/", "/register", "/privacy-policy", "/cookie-policy", "/public/**", "/ott/sent", "/actuator/**", "/css/**", "/ott/**", "/login/ott", "/not-verified").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .successHandler(loginSuccessHandler)
                        .permitAll()
                )
                .oneTimeTokenLogin(ott -> {
                    ott.tokenGenerationSuccessHandler(ottSuccessHandler);
                    ott.authenticationSuccessHandler((request, response, authentication) -> {
                        String username = authentication.getName();
                        System.out.println("OTT success: " + username);
                        appUserRepository.findByUsername(username).ifPresent(user -> {
                            user.setVerified(true);
                            appUserRepository.save(user);
                            System.out.println("Verified: " + user.isVerified());
                        });
                        response.sendRedirect("/products");
                    });
                });
        return http.build();
    }
}