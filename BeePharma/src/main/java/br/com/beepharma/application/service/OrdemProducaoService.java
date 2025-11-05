package br.com.beepharma.application.service;

import br.com.beepharma.application.dto.OrdemProducaoDTO;
import br.com.beepharma.domain.entity.OrdemProducao;
import br.com.beepharma.domain.entity.Produto;
import br.com.beepharma.domain.enums.OPStatus;
import br.com.beepharma.domain.repository.OrdemProducaoRepository;
import br.com.beepharma.domain.repository.ProdutoRepository;
import br.com.beepharma.util.mapper.OrdemProducaoMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrdemProducaoService {
    
    private final OrdemProducaoRepository ordemProducaoRepository;
    private final ProdutoRepository produtoRepository;
    private final OrdemProducaoMapper ordemProducaoMapper;
    
    @Transactional(readOnly = true)
    public List<OrdemProducaoDTO> listarTodas() {
        return ordemProducaoMapper.toDtoList(ordemProducaoRepository.findAll());
    }
    
    @Transactional(readOnly = true)
    public List<OrdemProducaoDTO> listarPorStatus(OPStatus status) {
        return ordemProducaoMapper.toDtoList(ordemProducaoRepository.findByStatus(status));
    }
    
    @Transactional(readOnly = true)
    public OrdemProducaoDTO buscarPorId(String id) {
        return ordemProducaoRepository.findById(UUID.fromString(id))
                .map(ordemProducaoMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Ordem de Produção não encontrada"));
    }
    
    @Transactional
    public OrdemProducaoDTO criar(OrdemProducaoDTO dto) {
        if (ordemProducaoRepository.existsByNumeroOP(dto.getNumeroOP())) {
            throw new IllegalArgumentException("Número de OP já cadastrado");
        }
        
        Produto produto = produtoRepository.findById(UUID.fromString(dto.getProdutoId()))
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado"));
        
        OrdemProducao op = ordemProducaoMapper.toEntity(dto);
        op.setProduto(produto);
        op.setStatus(OPStatus.PLANEJADA); // Status inicial
        
        op = ordemProducaoRepository.save(op);
        return ordemProducaoMapper.toDto(op);
    }
    
    @Transactional
    public OrdemProducaoDTO atualizar(String id, OrdemProducaoDTO dto) {
        OrdemProducao op = ordemProducaoRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new EntityNotFoundException("Ordem de Produção não encontrada"));
        
        if (!dto.getNumeroOP().equals(op.getNumeroOP()) 
                && ordemProducaoRepository.existsByNumeroOP(dto.getNumeroOP())) {
            throw new IllegalArgumentException("Número de OP já cadastrado");
        }
        
        ordemProducaoMapper.updateEntityFromDto(dto, op);
        op = ordemProducaoRepository.save(op);
        return ordemProducaoMapper.toDto(op);
    }
    
    @Transactional
    public OrdemProducaoDTO atualizarStatus(String id, OPStatus novoStatus) {
        OrdemProducao op = ordemProducaoRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new EntityNotFoundException("Ordem de Produção não encontrada"));
        
        op.setStatus(novoStatus);
        op = ordemProducaoRepository.save(op);
        return ordemProducaoMapper.toDto(op);
    }
}