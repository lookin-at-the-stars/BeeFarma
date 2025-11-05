package br.com.beepharma.domain.repository;

import br.com.beepharma.domain.entity.MovimentoEstoque;
import br.com.beepharma.domain.enums.MovimentoTipo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface MovimentoEstoqueRepository extends JpaRepository<MovimentoEstoque, UUID> {
    List<MovimentoEstoque> findByTipo(MovimentoTipo tipo);
    List<MovimentoEstoque> findByProdutoId(UUID produtoId);
    List<MovimentoEstoque> findByDataHoraBetween(LocalDateTime inicio, LocalDateTime fim);
}