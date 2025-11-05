package br.com.beepharma.domain.repository;

import br.com.beepharma.domain.entity.Lote;
import br.com.beepharma.domain.enums.LoteStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface LoteRepository extends JpaRepository<Lote, UUID> {
    List<Lote> findByStatus(LoteStatus status);
    List<Lote> findByDataValidadeBefore(LocalDate data);
    boolean existsByNumeroLote(String numeroLote);
}