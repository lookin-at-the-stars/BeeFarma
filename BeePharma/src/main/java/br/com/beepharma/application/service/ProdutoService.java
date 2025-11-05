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
        return produtoRepository.findById(UUID.fromString(id))
                .map(produtoMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado"));
    }
    
    @Transactional
    public ProdutoDTO criar(ProdutoDTO dto) {
        if (dto.getCodigoAnvisa() != null && produtoRepository.existsByCodigoAnvisa(dto.getCodigoAnvisa())) {
            throw new IllegalArgumentException("Código ANVISA já cadastrado");
        }
        
        Produto produto = produtoMapper.toEntity(dto);
        produto = produtoRepository.save(produto);
        return produtoMapper.toDto(produto);
    }
    
    @Transactional
    public ProdutoDTO atualizar(String id, ProdutoDTO dto) {
        Produto produto = produtoRepository.findById(UUID.fromString(id))
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
        if (!produtoRepository.existsById(UUID.fromString(id))) {
            throw new EntityNotFoundException("Produto não encontrado");
        }
        produtoRepository.deleteById(UUID.fromString(id));
    }
}
