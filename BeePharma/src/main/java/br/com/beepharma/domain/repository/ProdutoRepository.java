package br.com.beepharma.domain.repository;

import br.com.beepharma.domain.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, UUID> {
    boolean existsByCodigoAnvisa(String codigoAnvisa);
}