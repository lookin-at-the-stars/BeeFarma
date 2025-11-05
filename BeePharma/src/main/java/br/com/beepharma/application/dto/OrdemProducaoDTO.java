package br.com.beepharma.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class OrdemProducaoDTO {
    private String id;
    
    @NotBlank(message = "Número da OP é obrigatório")
    private String numeroOP;
    
    @NotNull(message = "ID do produto é obrigatório")
    private String produtoId;
    
    @NotNull(message = "Quantidade planejada é obrigatória")
    @Positive(message = "Quantidade planejada deve ser positiva")
    private BigDecimal quantidadePlanejada;
    
    private LocalDate dataInicio;
    private LocalDate dataFimPrevista;
    private String status;
}