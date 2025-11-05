package br.com.beepharma.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Home", description = "API inicial")
public class HomeController {

    @GetMapping("/")
    @Operation(summary = "Página inicial da API")
    public String home() {
        return "Bem-vindo à BeePharma API! Acesse /swagger-ui.html para a documentação completa.";
    }
}