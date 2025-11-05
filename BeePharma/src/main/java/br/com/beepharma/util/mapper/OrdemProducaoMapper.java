package br.com.beepharma.util.mapper;

import br.com.beepharma.application.dto.OrdemProducaoDTO;
import br.com.beepharma.domain.entity.OrdemProducao;
import br.com.beepharma.domain.enums.OPStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring", imports = {UUID.class, OPStatus.class})
public interface OrdemProducaoMapper {
    
    @Mapping(target = "id", source = "id")
    @Mapping(target = "produtoId", source = "produto.id")
    @Mapping(target = "status", source = "status")
    OrdemProducaoDTO toDto(OrdemProducao source);
    
    @Mapping(target = "id", source = "id")
    @Mapping(target = "produto.id", source = "produtoId")
    @Mapping(target = "status", source = "status")
    OrdemProducao toEntity(OrdemProducaoDTO source);
    
    List<OrdemProducaoDTO> toDtoList(List<OrdemProducao> entities);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "produto", ignore = true)
    void updateEntityFromDto(OrdemProducaoDTO source, @MappingTarget OrdemProducao target);

    default String uuidToString(UUID id) {
        return id == null ? null : id.toString();
    }

    default UUID stringToUuid(String id) {
        return id == null ? null : UUID.fromString(id);
    }
}