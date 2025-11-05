package br.com.beepharma.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class MovimentoEstoqueDTO {
    private String id;
    
    @NotBlank(message = "Tipo do movimento é obrigatório")
    private String tipo;
    
    @NotNull(message = "ID do produto é obrigatório")
    private String produtoId;
    
    @NotNull(message = "ID do lote é obrigatório")
    private String loteId;
    
    @NotNull(message = "Quantidade é obrigatória")
    @Positive(message = "Quantidade deve ser positiva")
    private BigDecimal quantidade;
    
    private LocalDateTime dataHora;
    
    @NotNull(message = "ID do responsável é obrigatório")
    private String responsavelId;
}