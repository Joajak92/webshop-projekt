package se.iths.joakim.webshopprojekt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.ott.RedirectOneTimeTokenGenerationSuccessHandler;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, OttSuccessHandler ottSuccessHandler) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/", "/register", "/privacy-policy", "/cookie-policy", "/public/**", "/ott/sent", "/actuator/**", "/css/**", "/ott/**", "/login/ott", "/ott/generate").permitAll()
                        .anyRequest().authenticated()
                )

                .formLogin(form ->
                        form.defaultSuccessUrl("/products", true)
                )
                .oneTimeTokenLogin(ott -> ott
                        .tokenGenerationSuccessHandler(ottSuccessHandler)
                );
        return http.build();
    }

    @Bean
    public RedirectOneTimeTokenGenerationSuccessHandler redirectOneTimeTokenGenerationSuccessHandler() {
        return new RedirectOneTimeTokenGenerationSuccessHandler("/ott/sent");
    }
}
