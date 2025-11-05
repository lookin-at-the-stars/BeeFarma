package br.com.beepharma.domain.entity;

import br.com.beepharma.domain.enums.LoteStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
@Table(name = "lotes")
public class Lote {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;
    
    @Column(name = "numero_lote", nullable = false, unique = true)
    private String numeroLote;
    
    @Column(name = "data_fabricacao", nullable = false)
    private LocalDate dataFabricacao;
    
    @Column(name = "data_validade", nullable = false)
    private LocalDate dataValidade;
    
    @Column(nullable = false)
    private BigDecimal quantidade;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LoteStatus status;
}