package br.com.beepharma.application.service;

import br.com.beepharma.application.dto.MovimentoEstoqueDTO;
import br.com.beepharma.domain.entity.Lote;
import br.com.beepharma.domain.entity.MovimentoEstoque;
import br.com.beepharma.domain.entity.Produto;
import br.com.beepharma.domain.enums.MovimentoTipo;
import br.com.beepharma.domain.repository.LoteRepository;
import br.com.beepharma.domain.repository.MovimentoEstoqueRepository;
import br.com.beepharma.domain.repository.ProdutoRepository;
import br.com.beepharma.util.mapper.MovimentoEstoqueMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MovimentoEstoqueService {
    
    private final MovimentoEstoqueRepository movimentoEstoqueRepository;
    private final ProdutoRepository produtoRepository;
    private final LoteRepository loteRepository;
    private final MovimentoEstoqueMapper movimentoEstoqueMapper;
    
    @Transactional(readOnly = true)
    public List<MovimentoEstoqueDTO> listarTodos() {
        return movimentoEstoqueMapper.toDtoList(movimentoEstoqueRepository.findAll());
    }
    
    @Transactional(readOnly = true)
    public List<MovimentoEstoqueDTO> listarPorTipo(MovimentoTipo tipo) {
        return movimentoEstoqueMapper.toDtoList(
            movimentoEstoqueRepository.findByTipo(tipo)
        );
    }
    
    @Transactional(readOnly = true)
    public List<MovimentoEstoqueDTO> listarPorProduto(String produtoId) {
        return movimentoEstoqueMapper.toDtoList(
            movimentoEstoqueRepository.findByProdutoId(UUID.fromString(produtoId))
        );
    }
    
    @Transactional(readOnly = true)
    public List<MovimentoEstoqueDTO> listarPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        return movimentoEstoqueMapper.toDtoList(
            movimentoEstoqueRepository.findByDataHoraBetween(inicio, fim)
        );
    }
    
    @Transactional(readOnly = true)
    public MovimentoEstoqueDTO buscarPorId(String id) {
        return movimentoEstoqueRepository.findById(UUID.fromString(id))
                .map(movimentoEstoqueMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Movimento de estoque não encontrado"));
    }
    
    @Transactional
    public MovimentoEstoqueDTO registrarMovimento(MovimentoEstoqueDTO dto) {
        Produto produto = produtoRepository.findById(UUID.fromString(dto.getProdutoId()))
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado"));
        
        Lote lote = loteRepository.findById(UUID.fromString(dto.getLoteId()))
                .orElseThrow(() -> new EntityNotFoundException("Lote não encontrado"));
        
        MovimentoEstoque movimento = movimentoEstoqueMapper.toEntity(dto);
        movimento.setProduto(produto);
        movimento.setLote(lote);
        movimento.setDataHora(LocalDateTime.now());
        
        movimento = movimentoEstoqueRepository.save(movimento);
        return movimentoEstoqueMapper.toDto(movimento);
    }
}