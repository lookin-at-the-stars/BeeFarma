package br.com.beepharma.application.service;

import br.com.beepharma.application.dto.ProdutoDTO;
import br.com.beepharma.domain.entity.Produto;
import br.com.beepharma.domain.repository.ProdutoRepository;
import br.com.beepharma.util.mapper.ProdutoMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProdutoService {
    
    private final ProdutoRepository produtoRepository;
    private final ProdutoMapper produtoMapper;
    
    @Transactional(readOnly = true)
    public List<ProdutoDTO> listarTodos() {
        return produtoMapper.toDtoList(produtoRepository.findAll());
    }
    
    @Transactional(readOnly = true)
    public ProdutoDTO buscarPorId(String id) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo");
        }
        UUID uuid = UUID.fromString(id);
        if (uuid == null) {
            throw new IllegalArgumentException("ID inválido");
        }
        return produtoRepository.findById(uuid)
                .map(produtoMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado"));
    }
    
    @Transactional
    public ProdutoDTO criar(ProdutoDTO dto) {
        validarProduto(dto);
        
        if (dto.getCodigoAnvisa() != null && produtoRepository.existsByCodigoAnvisa(dto.getCodigoAnvisa())) {
            throw new IllegalArgumentException("Código ANVISA já cadastrado");
        }
        
        Produto produto = produtoMapper.toEntity(dto);
        produto = produtoRepository.save(produto);
        return produtoMapper.toDto(produto);
    }
    
    @Transactional
    public ProdutoDTO atualizar(String id, ProdutoDTO dto) {
        validarProduto(dto);
        
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo");
        }
        UUID uuid = UUID.fromString(id);
        if (uuid == null) {
            throw new IllegalArgumentException("ID inválido");
        }
        Produto produto = produtoRepository.findById(uuid)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado"));
        
        if (dto.getCodigoAnvisa() != null && !dto.getCodigoAnvisa().equals(produto.getCodigoAnvisa()) 
                && produtoRepository.existsByCodigoAnvisa(dto.getCodigoAnvisa())) {
            throw new IllegalArgumentException("Código ANVISA já cadastrado");
        }
        
        produtoMapper.updateEntityFromDto(dto, produto);
        produto = produtoRepository.save(produto);
        return produtoMapper.toDto(produto);
    }
    
    @Transactional
    public void excluir(String id) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo");
        }
        UUID uuid = UUID.fromString(id);
        if (uuid == null) {
            throw new IllegalArgumentException("ID inválido");
        }
        if (!produtoRepository.existsById(uuid)) {
            throw new EntityNotFoundException("Produto não encontrado");
        }
        produtoRepository.deleteById(uuid);
    }
    
    private void validarProduto(ProdutoDTO dto) {
        if (dto.getNome() == null || dto.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do produto é obrigatório");
        }
        
        if (dto.getDescricao() != null && dto.getDescricao().length() > 1000) {
            throw new IllegalArgumentException("Descrição do produto não pode exceder 1000 caracteres");
        }
        
        if (dto.getPrincipioAtivo() == null || dto.getPrincipioAtivo().trim().isEmpty()) {
            throw new IllegalArgumentException("Princípio ativo é obrigatório");
        }
    }
}
