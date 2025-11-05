package br.com.beepharma.application.service;

import br.com.beepharma.application.dto.InventarioItemDTO;
import br.com.beepharma.domain.entity.InventarioItem;
import br.com.beepharma.domain.entity.Lote;
import br.com.beepharma.domain.entity.Produto;
import br.com.beepharma.domain.repository.InventarioItemRepository;
import br.com.beepharma.domain.repository.LoteRepository;
import br.com.beepharma.domain.repository.ProdutoRepository;
import br.com.beepharma.util.mapper.InventarioItemMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InventarioItemService {
    
    private final InventarioItemRepository inventarioItemRepository;
    private final ProdutoRepository produtoRepository;
    private final LoteRepository loteRepository;
    private final InventarioItemMapper inventarioItemMapper;
    
    @Transactional(readOnly = true)
    public List<InventarioItemDTO> listarTodos() {
        return inventarioItemMapper.toDtoList(inventarioItemRepository.findAll());
    }
    
    @Transactional(readOnly = true)
    public List<InventarioItemDTO> listarPorProduto(String produtoId) {
        return inventarioItemMapper.toDtoList(
            inventarioItemRepository.findByProdutoId(UUID.fromString(produtoId))
        );
    }
    
    @Transactional(readOnly = true)
    public List<InventarioItemDTO> listarPorLocalizacao(String localizacao) {
        return inventarioItemMapper.toDtoList(
            inventarioItemRepository.findByLocalizacao(localizacao)
        );
    }
    
    @Transactional(readOnly = true)
    public InventarioItemDTO buscarPorId(String id) {
        return inventarioItemRepository.findById(UUID.fromString(id))
                .map(inventarioItemMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Item do inventário não encontrado"));
    }
    
    @Transactional
    public InventarioItemDTO criar(InventarioItemDTO dto) {
        Produto produto = produtoRepository.findById(UUID.fromString(dto.getProdutoId()))
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado"));
        
        Lote lote = loteRepository.findById(UUID.fromString(dto.getLoteId()))
                .orElseThrow(() -> new EntityNotFoundException("Lote não encontrado"));
        
        InventarioItem item = inventarioItemMapper.toEntity(dto);
        item.setProduto(produto);
        item.setLote(lote);
        
        item = inventarioItemRepository.save(item);
        return inventarioItemMapper.toDto(item);
    }
    
    @Transactional
    public InventarioItemDTO atualizar(String id, InventarioItemDTO dto) {
        InventarioItem item = inventarioItemRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new EntityNotFoundException("Item do inventário não encontrado"));
        
        inventarioItemMapper.updateEntityFromDto(dto, item);
        item = inventarioItemRepository.save(item);
        return inventarioItemMapper.toDto(item);
    }
    
    @Transactional
    public void excluir(String id) {
        if (!inventarioItemRepository.existsById(UUID.fromString(id))) {
            throw new EntityNotFoundException("Item do inventário não encontrado");
        }
        inventarioItemRepository.deleteById(UUID.fromString(id));
    }
}