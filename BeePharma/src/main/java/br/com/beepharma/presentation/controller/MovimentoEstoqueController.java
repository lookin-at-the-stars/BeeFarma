package br.com.beepharma.presentation.controller;

import br.com.beepharma.application.dto.MovimentoEstoqueDTO;
import br.com.beepharma.application.service.MovimentoEstoqueService;
import br.com.beepharma.domain.enums.MovimentoTipo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/movimentos-estoque")
@RequiredArgsConstructor
@Tag(name = "Movimentos de Estoque", description = "API para gerenciamento de movimentos de estoque")
public class MovimentoEstoqueController {
    
    private final MovimentoEstoqueService movimentoEstoqueService;
    
    @GetMapping
    @Operation(summary = "Listar todos os movimentos de estoque")
    public ResponseEntity<List<MovimentoEstoqueDTO>> listarTodos() {
        return ResponseEntity.ok(movimentoEstoqueService.listarTodos());
    }
    
    @GetMapping("/tipo/{tipo}")
    @Operation(summary = "Listar movimentos por tipo")
    public ResponseEntity<List<MovimentoEstoqueDTO>> listarPorTipo(@PathVariable MovimentoTipo tipo) {
        return ResponseEntity.ok(movimentoEstoqueService.listarPorTipo(tipo));
    }
    
    @GetMapping("/produto/{produtoId}")
    @Operation(summary = "Listar movimentos por produto")
    public ResponseEntity<List<MovimentoEstoqueDTO>> listarPorProduto(@PathVariable String produtoId) {
        return ResponseEntity.ok(movimentoEstoqueService.listarPorProduto(produtoId));
    }
    
    @GetMapping("/periodo")
    @Operation(summary = "Listar movimentos por período")
    public ResponseEntity<List<MovimentoEstoqueDTO>> listarPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim) {
        return ResponseEntity.ok(movimentoEstoqueService.listarPorPeriodo(inicio, fim));
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Buscar movimento por ID")
    public ResponseEntity<MovimentoEstoqueDTO> buscarPorId(@PathVariable String id) {
        return ResponseEntity.ok(movimentoEstoqueService.buscarPorId(id));
    }
    
    @PostMapping
    @Operation(summary = "Registrar novo movimento de estoque")
    public ResponseEntity<MovimentoEstoqueDTO> registrarMovimento(@Valid @RequestBody MovimentoEstoqueDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(movimentoEstoqueService.registrarMovimento(dto));
    }
}