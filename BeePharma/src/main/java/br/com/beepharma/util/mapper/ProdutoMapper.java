package br.com.beepharma.util.mapper;

import br.com.beepharma.application.dto.ProdutoDTO;
import br.com.beepharma.domain.entity.Produto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface ProdutoMapper {
    
    ProdutoDTO toDto(Produto entity);
    
    @Mapping(target = "criadoEm", ignore = true)
    Produto toEntity(ProdutoDTO dto);
    
    List<ProdutoDTO> toDtoList(List<Produto> entities);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "criadoEm", ignore = true)
    void updateEntityFromDto(ProdutoDTO dto, @MappingTarget Produto entity);

    default String uuidToString(UUID id) {
        return id == null ? null : id.toString();
    }

    default UUID stringToUuid(String id) {
        return id == null ? null : UUID.fromString(id);
    }
}
