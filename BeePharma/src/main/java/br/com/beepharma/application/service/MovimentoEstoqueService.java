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
        if (produtoId == null) {
            throw new IllegalArgumentException("ID do produto não pode ser nulo");
        }
        UUID uuid = UUID.fromString(produtoId);
        if (uuid == null) {
            throw new IllegalArgumentException("ID do produto inválido");
        }
        return movimentoEstoqueMapper.toDtoList(
            movimentoEstoqueRepository.findByProdutoId(uuid)
        );
    }
    
    @Transactional(readOnly = true)
    public List<MovimentoEstoqueDTO> listarPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        if (inicio == null || fim == null) {
            throw new IllegalArgumentException("Data inicial e final são obrigatórias");
        }
        if (inicio.isAfter(fim)) {
            throw new IllegalArgumentException("Data inicial não pode ser posterior à data final");
        }
        return movimentoEstoqueMapper.toDtoList(
            movimentoEstoqueRepository.findByDataHoraBetween(inicio, fim)
        );
    }
    
    @Transactional(readOnly = true)
    public MovimentoEstoqueDTO buscarPorId(String id) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo");
        }
        UUID uuid = UUID.fromString(id);
        if (uuid == null) {
            throw new IllegalArgumentException("ID inválido");
        }
        return movimentoEstoqueRepository.findById(uuid)
                .map(movimentoEstoqueMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Movimento de estoque não encontrado"));
    }
    
    @Transactional
    public MovimentoEstoqueDTO registrarMovimento(MovimentoEstoqueDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Dados do movimento não podem ser nulos");
        }
        if (dto.getQuantidade() == null || dto.getQuantidade().signum() <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser maior que zero");
        }
        if (dto.getTipo() == null) {
            throw new IllegalArgumentException("Tipo do movimento é obrigatório");
        }
        if (dto.getProdutoId() == null) {
            throw new IllegalArgumentException("ID do produto não pode ser nulo");
        }
        if (dto.getLoteId() == null) {
            throw new IllegalArgumentException("ID do lote não pode ser nulo");
        }

        UUID produtoUuid = UUID.fromString(dto.getProdutoId());
        UUID loteUuid = UUID.fromString(dto.getLoteId());
        
        if (produtoUuid == null) {
            throw new IllegalArgumentException("ID do produto inválido");
        }
        if (loteUuid == null) {
            throw new IllegalArgumentException("ID do lote inválido");
        }

        Produto produto = produtoRepository.findById(produtoUuid)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado"));
        
        Lote lote = loteRepository.findById(loteUuid)
                .orElseThrow(() -> new EntityNotFoundException("Lote não encontrado"));
        
        // Validar se o lote pertence ao produto
        if (!lote.getProduto().getId().equals(produto.getId())) {
            throw new IllegalArgumentException("O lote informado não pertence ao produto selecionado");
        }
        
        MovimentoEstoque movimento = movimentoEstoqueMapper.toEntity(dto);
        movimento.setProduto(produto);
        movimento.setLote(lote);
        movimento.setDataHora(LocalDateTime.now());
        
        // Atualizar quantidade do lote
        if (MovimentoTipo.SAIDA.equals(dto.getTipo())) {
            if (lote.getQuantidade().compareTo(dto.getQuantidade()) < 0) {
                throw new IllegalStateException("Quantidade insuficiente no lote");
            }
            lote.setQuantidade(lote.getQuantidade().subtract(dto.getQuantidade()));
        } else {
            lote.setQuantidade(lote.getQuantidade().add(dto.getQuantidade()));
        }
        
        loteRepository.save(lote);
        movimento = movimentoEstoqueRepository.save(movimento);
        return movimentoEstoqueMapper.toDto(movimento);
    }
}