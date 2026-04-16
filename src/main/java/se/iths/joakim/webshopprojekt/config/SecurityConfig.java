package se.iths.joakim.webshopprojekt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.security.authentication.ott.JdbcOneTimeTokenService;
import org.springframework.security.authentication.ott.OneTimeTokenService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public org.springframework.security.authentication.ott.OneTimeTokenService jdbcOneTimeTokenService(JdbcOperations jdbcOperations) {
        return new JdbcOneTimeTokenService(jdbcOperations);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, OttSuccessHandler ottSuccessHandler) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/", "/register", "/privacy-policy", "/cookie-policy", "/public/**", "/ott/sent", "/actuator/**", "/css/**", "/ott/**", "/login/ott").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .defaultSuccessUrl("/products", true)
                        .permitAll()
                )
                .oneTimeTokenLogin(ott -> {
                    ott.tokenGenerationSuccessHandler(ottSuccessHandler);
                    ott.authenticationSuccessHandler(new org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler("/products"));
                });
        return http.build();
    }
}