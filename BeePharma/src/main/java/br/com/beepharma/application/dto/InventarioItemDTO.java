package br.com.beepharma.application.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class InventarioItemDTO {
    private String id;
    
    @NotNull(message = "ID do produto é obrigatório")
    private String produtoId;
    
    @NotNull(message = "ID do lote é obrigatório")
    private String loteId;
    
    @NotNull(message = "Quantidade é obrigatória")
    @Positive(message = "Quantidade deve ser positiva")
    private BigDecimal quantidade;
    
    @NotNull(message = "Localização é obrigatória")
    private String localizacao;
}