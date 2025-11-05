package br.com.beepharma.util.mapper;

import br.com.beepharma.application.dto.OrdemProducaoDTO;
import br.com.beepharma.domain.entity.OrdemProducao;
import br.com.beepharma.domain.entity.Produto;
import br.com.beepharma.domain.enums.OPStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring", imports = {UUID.class, OPStatus.class})
public interface OrdemProducaoMapper {
    
    @Mapping(target = "id", expression = "java(source.getId() != null ? source.getId().toString() : null)")
    @Mapping(target = "produtoId", expression = "java(source.getProduto().getId().toString())")
    @Mapping(target = "status", expression = "java(source.getStatus().name())")
    OrdemProducaoDTO toDto(OrdemProducao source);
    
    @Mapping(target = "id", expression = "java(source.getId() != null ? UUID.fromString(source.getId()) : null)")
    @Mapping(target = "produto", expression = "java(new Produto())")
    @Mapping(target = "produto.id", expression = "java(UUID.fromString(source.getProdutoId()))")
    @Mapping(target = "status", expression = "java(OPStatus.valueOf(source.getStatus()))")
    OrdemProducao toEntity(OrdemProducaoDTO source);
    
    List<OrdemProducaoDTO> toDtoList(List<OrdemProducao> entities);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "produto", ignore = true)
    void updateEntityFromDto(OrdemProducaoDTO dto, @MappingTarget OrdemProducao entity);
}