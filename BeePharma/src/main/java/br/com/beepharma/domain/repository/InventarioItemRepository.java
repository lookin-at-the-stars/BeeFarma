package br.com.beepharma.domain.repository;

import br.com.beepharma.domain.entity.InventarioItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface InventarioItemRepository extends JpaRepository<InventarioItem, UUID> {
    List<InventarioItem> findByProdutoId(UUID produtoId);
    List<InventarioItem> findByLocalizacao(String localizacao);
}