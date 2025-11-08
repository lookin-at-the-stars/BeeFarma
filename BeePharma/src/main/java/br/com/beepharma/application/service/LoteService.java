package br.com.beepharma.application.service;

import br.com.beepharma.application.dto.LoteDTO;
import br.com.beepharma.domain.entity.Lote;
import br.com.beepharma.domain.entity.Produto;
import br.com.beepharma.domain.enums.LoteStatus;
import br.com.beepharma.domain.repository.LoteRepository;
import br.com.beepharma.domain.repository.ProdutoRepository;
import br.com.beepharma.util.mapper.LoteMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LoteService {
    
    private final LoteRepository loteRepository;
    private final ProdutoRepository produtoRepository;
    private final LoteMapper loteMapper;
    
    @Transactional(readOnly = true)
    public List<LoteDTO> listarTodos() {
        return loteMapper.toDtoList(loteRepository.findAll());
    }
    
    @Transactional(readOnly = true)
    public List<LoteDTO> listarPorStatus(LoteStatus status) {
        return loteMapper.toDtoList(loteRepository.findByStatus(status));
    }
    
    @Transactional(readOnly = true)
    public LoteDTO buscarPorId(String id) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo");
        }
        UUID uuid = UUID.fromString(id);
        if (uuid == null) {
            throw new IllegalArgumentException("ID inválido");
        }
        return loteRepository.findById(uuid)
                .map(loteMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Lote não encontrado"));
    }
    
    @Transactional
    public LoteDTO criar(LoteDTO dto) {
        validarLote(dto);
        
        if (dto.getProdutoId() == null) {
            throw new IllegalArgumentException("ID do produto não pode ser nulo");
        }
        UUID produtoUuid = UUID.fromString(dto.getProdutoId());
        if (produtoUuid == null) {
            throw new IllegalArgumentException("ID do produto inválido");
        }
        Produto produto = produtoRepository.findById(produtoUuid)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado"));
        
        if (loteRepository.existsByNumeroLote(dto.getNumeroLote())) {
            throw new IllegalArgumentException("Número de lote já cadastrado");
        }
        
        Lote lote = loteMapper.toEntity(dto);
        lote.setProduto(produto);
        lote.setStatus(LoteStatus.QUARENTENA); // Status inicial padrão
        
        lote = loteRepository.save(lote);
        return loteMapper.toDto(lote);
    }
    
    @Transactional
    public LoteDTO atualizar(String id, LoteDTO dto) {
        validarLote(dto);
        
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo");
        }
        UUID uuid = UUID.fromString(id);
        if (uuid == null) {
            throw new IllegalArgumentException("ID inválido");
        }
        Lote lote = loteRepository.findById(uuid)
                .orElseThrow(() -> new EntityNotFoundException("Lote não encontrado"));
        
        if (!dto.getNumeroLote().equals(lote.getNumeroLote()) 
                && loteRepository.existsByNumeroLote(dto.getNumeroLote())) {
            throw new IllegalArgumentException("Número de lote já cadastrado");
        }
        
        loteMapper.updateEntityFromDto(dto, lote);
        lote = loteRepository.save(lote);
        return loteMapper.toDto(lote);
    }
    
    @Transactional
    public LoteDTO atualizarStatus(String id, LoteStatus novoStatus) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo");
        }
        UUID uuid = UUID.fromString(id);
        if (uuid == null) {
            throw new IllegalArgumentException("ID inválido");
        }
        Lote lote = loteRepository.findById(uuid)
                .orElseThrow(() -> new EntityNotFoundException("Lote não encontrado"));
        
        lote.setStatus(novoStatus);
        lote = loteRepository.save(lote);
        return loteMapper.toDto(lote);
    }
    
    private void validarLote(LoteDTO dto) {
        if (dto.getDataFabricacao() != null && dto.getDataValidade() != null 
                && dto.getDataFabricacao().isAfter(dto.getDataValidade())) {
            throw new IllegalArgumentException("Data de fabricação não pode ser posterior à data de validade");
        }
        
        if (dto.getDataFabricacao() != null && dto.getDataFabricacao().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Data de fabricação não pode ser futura");
        }
    }
}