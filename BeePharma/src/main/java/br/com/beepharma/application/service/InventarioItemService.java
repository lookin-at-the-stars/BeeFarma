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
        if (produtoId == null) {
            throw new IllegalArgumentException("ID do produto não pode ser nulo");
        }
        UUID uuid = UUID.fromString(produtoId);
        if (uuid == null) {
            throw new IllegalArgumentException("ID do produto inválido");
        }
        return inventarioItemMapper.toDtoList(
            inventarioItemRepository.findByProdutoId(uuid)
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
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo");
        }
        UUID uuid = UUID.fromString(id);
        if (uuid == null) {
            throw new IllegalArgumentException("ID inválido");
        }
        return inventarioItemRepository.findById(uuid)
                .map(inventarioItemMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Item do inventário não encontrado"));
    }
    
    @Transactional
    public InventarioItemDTO criar(InventarioItemDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Dados do item não podem ser nulos");
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
        
        InventarioItem item = inventarioItemMapper.toEntity(dto);
        item.setProduto(produto);
        item.setLote(lote);
        
        item = inventarioItemRepository.save(item);
        return inventarioItemMapper.toDto(item);
    }
    
    @Transactional
    public InventarioItemDTO atualizar(String id, InventarioItemDTO dto) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo");
        }
        UUID uuid = UUID.fromString(id);
        if (uuid == null) {
            throw new IllegalArgumentException("ID inválido");
        }
        InventarioItem item = inventarioItemRepository.findById(uuid)
                .orElseThrow(() -> new EntityNotFoundException("Item do inventário não encontrado"));
        
        inventarioItemMapper.updateEntityFromDto(dto, item);
        item = inventarioItemRepository.save(item);
        return inventarioItemMapper.toDto(item);
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
        if (!inventarioItemRepository.existsById(uuid)) {
            throw new EntityNotFoundException("Item do inventário não encontrado");
        }
        inventarioItemRepository.deleteById(uuid);
    }
}