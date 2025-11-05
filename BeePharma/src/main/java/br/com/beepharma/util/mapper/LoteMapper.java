package br.com.beepharma.util.mapper;

import br.com.beepharma.application.dto.LoteDTO;
import br.com.beepharma.domain.entity.Lote;
import br.com.beepharma.domain.enums.LoteStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring", imports = {LoteStatus.class})
public interface LoteMapper {
    
    @Mapping(target = "id", source = "id")
    @Mapping(target = "produtoId", source = "produto.id")
    @Mapping(target = "status", source = "status")
    LoteDTO toDto(Lote source);
    
    @Mapping(target = "id", source = "id")
    @Mapping(target = "produto.id", source = "produtoId")
    @Mapping(target = "status", source = "status")
    Lote toEntity(LoteDTO source);
    
    List<LoteDTO> toDtoList(List<Lote> entities);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "produto", ignore = true)
    void updateEntityFromDto(LoteDTO dto, @MappingTarget Lote entity);

    default String uuidToString(UUID id) {
        return id == null ? null : id.toString();
    }

    default UUID stringToUuid(String id) {
        return id == null ? null : UUID.fromString(id);
    }
}