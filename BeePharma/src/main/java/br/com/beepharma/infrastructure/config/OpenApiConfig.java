package br.com.beepharma.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI beePharmaOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("BeePharma API")
                        .description("Sistema de gestão farmacêutica com controle de lotes e rastreabilidade")
                        .version("1.0.0")
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org")));
    }
}