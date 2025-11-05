package br.com.beepharma;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class BeePharmaApplication {

    public static void main(String[] args) {
        SpringApplication.run(BeePharmaApplication.class, args);
    }
}