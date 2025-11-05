package br.com.beepharma.domain.entity;

import br.com.beepharma.domain.enums.OPStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
@Table(name = "ordens_producao")
public class OrdemProducao {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(name = "numero_op", nullable = false, unique = true)
    private String numeroOP;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;
    
    @Column(name = "quantidade_planejada", nullable = false)
    private BigDecimal quantidadePlanejada;
    
    @Column(name = "data_inicio")
    private LocalDate dataInicio;
    
    @Column(name = "data_fim_prevista")
    private LocalDate dataFimPrevista;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OPStatus status;
}