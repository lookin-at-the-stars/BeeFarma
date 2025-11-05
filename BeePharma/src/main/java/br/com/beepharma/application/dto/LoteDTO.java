package br.com.beepharma.application.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class LoteDTO {
    private String id;
    
    @NotBlank(message = "Número do lote é obrigatório")
    private String numeroLote;
    
    @NotNull(message = "ID do produto é obrigatório")
    private String produtoId;
    
    @NotNull(message = "Data de fabricação é obrigatória")
    private LocalDate dataFabricacao;
    
    @NotNull(message = "Data de validade é obrigatória")
    @Future(message = "Data de validade deve ser futura")
    private LocalDate dataValidade;
    
    @NotNull(message = "Quantidade é obrigatória")
    @Positive(message = "Quantidade deve ser positiva")
    private BigDecimal quantidade;
    
    private String status;
}