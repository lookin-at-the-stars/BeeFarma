package br.com.beepharma.util.mapper;

import br.com.beepharma.application.dto.InventarioItemDTO;
import br.com.beepharma.domain.entity.InventarioItem;
import br.com.beepharma.domain.entity.Lote;
import br.com.beepharma.domain.entity.Produto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring", imports = UUID.class)
public interface InventarioItemMapper {
    
    @Mapping(target = "id", expression = "java(source.getId() != null ? source.getId().toString() : null)")
    @Mapping(target = "produtoId", expression = "java(source.getProduto().getId().toString())")
    @Mapping(target = "loteId", expression = "java(source.getLote().getId().toString())")
    InventarioItemDTO toDto(InventarioItem source);
    
    @Mapping(target = "id", expression = "java(source.getId() != null ? UUID.fromString(source.getId()) : null)")
    @Mapping(target = "produto", expression = "java(new Produto())")
    @Mapping(target = "produto.id", expression = "java(UUID.fromString(source.getProdutoId()))")
    @Mapping(target = "lote", expression = "java(new Lote())")
    @Mapping(target = "lote.id", expression = "java(UUID.fromString(source.getLoteId()))")
    InventarioItem toEntity(InventarioItemDTO source);
    
    List<InventarioItemDTO> toDtoList(List<InventarioItem> entities);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "produto", ignore = true)
    @Mapping(target = "lote", ignore = true)
    void updateEntityFromDto(InventarioItemDTO source, @MappingTarget InventarioItem target);
}