package br.com.beepharma.util.mapper;

import br.com.beepharma.application.dto.LoteDTO;
import br.com.beepharma.domain.entity.Lote;
import br.com.beepharma.domain.entity.Produto;
import br.com.beepharma.domain.enums.LoteStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring", imports = {UUID.class, LoteStatus.class})
public interface LoteMapper {
    
    @Mapping(target = "id", expression = "java(source.getId() != null ? source.getId().toString() : null)")
    @Mapping(target = "produtoId", expression = "java(source.getProduto().getId().toString())")
    @Mapping(target = "status", expression = "java(source.getStatus().name())")
    LoteDTO toDto(Lote source);
    
    @Mapping(target = "id", expression = "java(source.getId() != null ? UUID.fromString(source.getId()) : null)")
    @Mapping(target = "produto", expression = "java(new Produto())")
    @Mapping(target = "produto.id", expression = "java(UUID.fromString(source.getProdutoId()))")
    @Mapping(target = "status", expression = "java(LoteStatus.valueOf(source.getStatus()))")
    Lote toEntity(LoteDTO source);
    
    List<LoteDTO> toDtoList(List<Lote> entities);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "produto", ignore = true)
    void updateEntityFromDto(LoteDTO dto, @MappingTarget Lote entity);
}