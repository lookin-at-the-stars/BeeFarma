package br.com.beepharma.util.mapper;

import br.com.beepharma.application.dto.MovimentoEstoqueDTO;
import br.com.beepharma.domain.entity.MovimentoEstoque;
import br.com.beepharma.domain.enums.MovimentoTipo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring", imports = {MovimentoTipo.class})
public interface MovimentoEstoqueMapper {
    
    @Mapping(target = "id", source = "id")
    @Mapping(target = "produtoId", source = "produto.id")
    @Mapping(target = "loteId", source = "lote.id")
    @Mapping(target = "responsavelId", source = "responsavel.id")
    @Mapping(target = "tipo", source = "tipo")
    MovimentoEstoqueDTO toDto(MovimentoEstoque source);
    
    @Mapping(target = "id", source = "id")
    @Mapping(target = "produto.id", source = "produtoId")
    @Mapping(target = "lote.id", source = "loteId")
    @Mapping(target = "responsavel.id", source = "responsavelId")
    @Mapping(target = "tipo", source = "tipo")
    MovimentoEstoque toEntity(MovimentoEstoqueDTO source);
    
    List<MovimentoEstoqueDTO> toDtoList(List<MovimentoEstoque> entities);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "produto", ignore = true)
    @Mapping(target = "lote", ignore = true)
    @Mapping(target = "responsavel", ignore = true)
    @Mapping(target = "dataHora", ignore = true)
    void updateEntityFromDto(MovimentoEstoqueDTO dto, @MappingTarget MovimentoEstoque entity);

    default String uuidToString(UUID id) {
        return id == null ? null : id.toString();
    }

    default UUID stringToUuid(String id) {
        return id == null ? null : UUID.fromString(id);
    }
}