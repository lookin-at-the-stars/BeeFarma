package br.com.beepharma.util.mapper;

import br.com.beepharma.application.dto.InventarioItemDTO;
import br.com.beepharma.domain.entity.InventarioItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring", imports = UUID.class)
public interface InventarioItemMapper {
    
    @Mapping(target = "id", source = "id")
    @Mapping(target = "produtoId", source = "produto.id")
    @Mapping(target = "loteId", source = "lote.id")
    InventarioItemDTO toDto(InventarioItem source);
    
    @Mapping(target = "id", source = "id")
    @Mapping(target = "produto.id", source = "produtoId")
    @Mapping(target = "lote.id", source = "loteId")
    InventarioItem toEntity(InventarioItemDTO source);
    
    List<InventarioItemDTO> toDtoList(List<InventarioItem> entities);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "produto", ignore = true)
    @Mapping(target = "lote", ignore = true)
    void updateEntityFromDto(InventarioItemDTO source, @MappingTarget InventarioItem target);

    default String uuidToString(UUID id) {
        return id == null ? null : id.toString();
    }

    default UUID stringToUuid(String id) {
        return id == null ? null : UUID.fromString(id);
    }
}