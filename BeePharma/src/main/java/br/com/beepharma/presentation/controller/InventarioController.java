package br.com.beepharma.presentation.controller;

import br.com.beepharma.application.dto.InventarioItemDTO;
import br.com.beepharma.application.service.InventarioItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventario")
@RequiredArgsConstructor
@Tag(name = "Inventário", description = "API para gerenciamento do inventário")
public class InventarioController {
    
    private final InventarioItemService inventarioItemService;
    
    @GetMapping
    @Operation(summary = "Listar todos os itens do inventário")
    public ResponseEntity<List<InventarioItemDTO>> listarTodos() {
        return ResponseEntity.ok(inventarioItemService.listarTodos());
    }
    
    @GetMapping("/produto/{produtoId}")
    @Operation(summary = "Listar itens do inventário por produto")
    public ResponseEntity<List<InventarioItemDTO>> listarPorProduto(@PathVariable String produtoId) {
        return ResponseEntity.ok(inventarioItemService.listarPorProduto(produtoId));
    }
    
    @GetMapping("/localizacao/{localizacao}")
    @Operation(summary = "Listar itens do inventário por localização")
    public ResponseEntity<List<InventarioItemDTO>> listarPorLocalizacao(@PathVariable String localizacao) {
        return ResponseEntity.ok(inventarioItemService.listarPorLocalizacao(localizacao));
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Buscar item do inventário por ID")
    public ResponseEntity<InventarioItemDTO> buscarPorId(@PathVariable String id) {
        return ResponseEntity.ok(inventarioItemService.buscarPorId(id));
    }
    
    @PostMapping
    @Operation(summary = "Criar novo item no inventário")
    public ResponseEntity<InventarioItemDTO> criar(@Valid @RequestBody InventarioItemDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(inventarioItemService.criar(dto));
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar item do inventário existente")
    public ResponseEntity<InventarioItemDTO> atualizar(
            @PathVariable String id,
            @Valid @RequestBody InventarioItemDTO dto) {
        return ResponseEntity.ok(inventarioItemService.atualizar(id, dto));
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir item do inventário")
    public ResponseEntity<Void> excluir(@PathVariable String id) {
        inventarioItemService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}