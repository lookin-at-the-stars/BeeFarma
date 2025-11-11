package br.com.beepharma.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ProdutoDTO {
    private String id;
    
    @NotBlank(message = "Nome é obrigatório")
    private String nome;
    
    private String descricao;
    
    @Pattern(regexp = "^[0-9]{1,13}$", message = "Código ANVISA inválido")
    private String codigoAnvisa;
    
    @NotBlank(message = "Unidade é obrigatória")
    private String unidade;
    
    @NotBlank(message = "Princípio ativo é obrigatório")
    private String principioAtivo;
    
    private String classeTerapeutica;
}