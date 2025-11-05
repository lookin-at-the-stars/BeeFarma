package br.com.beepharma.domain.entity;

import br.com.beepharma.domain.enums.MovimentoTipo;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
@Table(name = "movimentos_estoque")
public class MovimentoEstoque {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MovimentoTipo tipo;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lote_id", nullable = false)
    private Lote lote;
    
    @Column(nullable = false)
    private BigDecimal quantidade;
    
    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "responsavel_id", nullable = false)
    private Usuario responsavel;
    
    @PrePersist
    protected void onCreate() {
        dataHora = LocalDateTime.now();
    }
}