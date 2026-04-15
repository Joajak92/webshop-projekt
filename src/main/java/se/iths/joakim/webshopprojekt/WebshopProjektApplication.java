package se.iths.joakim.webshopprojekt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
        "se.iths.joakim.webshopprojekt",
        "se.iths.joakim.springmessenger"
})
public class WebshopProjektApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebshopProjektApplication.class, args);
    }

}
