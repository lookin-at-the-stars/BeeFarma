package br.com.beepharma.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
@Table(name = "produtos")
public class Produto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(nullable = false)
    private String nome;
    
    @Column(length = 1000)
    private String descricao;
    
    @Column(name = "codigo_anvisa", unique = true)
    private String codigoAnvisa;
    
    @Column(nullable = false)
    private String unidade;
    
    @Column(name = "principio_ativo", nullable = false)
    private String principioAtivo;
    
    @Column(name = "classe_terapeutica")
    private String classeTerapeutica;
    
    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDate criadoEm;
    
    @PrePersist
    protected void onCreate() {
        criadoEm = LocalDate.now();
    }
}