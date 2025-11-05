package br.com.beepharma.domain.repository;

import br.com.beepharma.domain.entity.OrdemProducao;
import br.com.beepharma.domain.enums.OPStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrdemProducaoRepository extends JpaRepository<OrdemProducao, UUID> {
    List<OrdemProducao> findByStatus(OPStatus status);
    boolean existsByNumeroOP(String numeroOP);
}