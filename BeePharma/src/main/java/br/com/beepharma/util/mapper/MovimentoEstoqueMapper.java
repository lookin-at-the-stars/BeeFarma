package br.com.beepharma.util.mapper;

import br.com.beepharma.application.dto.MovimentoEstoqueDTO;
import br.com.beepharma.domain.entity.Lote;
import br.com.beepharma.domain.entity.MovimentoEstoque;
import br.com.beepharma.domain.entity.Produto;
import br.com.beepharma.domain.entity.Usuario;
import br.com.beepharma.domain.enums.MovimentoTipo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring", imports = {UUID.class, MovimentoTipo.class})
public interface MovimentoEstoqueMapper {
    
    @Mapping(target = "id", expression = "java(source.getId() != null ? source.getId().toString() : null)")
    @Mapping(target = "produtoId", expression = "java(source.getProduto().getId().toString())")
    @Mapping(target = "loteId", expression = "java(source.getLote().getId().toString())")
    @Mapping(target = "responsavelId", expression = "java(source.getResponsavel().getId().toString())")
    @Mapping(target = "tipo", expression = "java(source.getTipo().name())")
    MovimentoEstoqueDTO toDto(MovimentoEstoque source);
    
    @Mapping(target = "id", expression = "java(source.getId() != null ? UUID.fromString(source.getId()) : null)")
    @Mapping(target = "produto", expression = "java(new Produto())")
    @Mapping(target = "produto.id", expression = "java(UUID.fromString(source.getProdutoId()))")
    @Mapping(target = "lote", expression = "java(new Lote())")
    @Mapping(target = "lote.id", expression = "java(UUID.fromString(source.getLoteId()))")
    @Mapping(target = "responsavel", expression = "java(new Usuario())")
    @Mapping(target = "responsavel.id", expression = "java(UUID.fromString(source.getResponsavelId()))")
    @Mapping(target = "tipo", expression = "java(MovimentoTipo.valueOf(source.getTipo()))")
    MovimentoEstoque toEntity(MovimentoEstoqueDTO source);
    
    List<MovimentoEstoqueDTO> toDtoList(List<MovimentoEstoque> entities);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "produto", ignore = true)
    @Mapping(target = "lote", ignore = true)
    @Mapping(target = "responsavel", ignore = true)
    @Mapping(target = "dataHora", ignore = true)
    void updateEntityFromDto(MovimentoEstoqueDTO dto, @MappingTarget MovimentoEstoque entity);
}