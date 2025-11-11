package br.com.beepharma.presentation.controller;

import br.com.beepharma.application.dto.LoteDTO;
import br.com.beepharma.application.service.LoteService;
import br.com.beepharma.domain.enums.LoteStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lotes")
@RequiredArgsConstructor
@Tag(name = "Lotes", description = "API para gerenciamento de lotes")
public class LoteController {
    
    private final LoteService loteService;
    
    @GetMapping
    @Operation(summary = "Listar todos os lotes")
    public ResponseEntity<List<LoteDTO>> listarTodos() {
        return ResponseEntity.ok(loteService.listarTodos());
    }
    
    @GetMapping("/status/{status}")
    @Operation(summary = "Listar lotes por status")
    public ResponseEntity<List<LoteDTO>> listarPorStatus(@PathVariable LoteStatus status) {
        return ResponseEntity.ok(loteService.listarPorStatus(status));
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Buscar lote por ID")
    public ResponseEntity<LoteDTO> buscarPorId(@PathVariable String id) {
        return ResponseEntity.ok(loteService.buscarPorId(id));
    }
    
    @PostMapping
    @Operation(summary = "Criar novo lote")
    public ResponseEntity<LoteDTO> criar(@Valid @RequestBody LoteDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(loteService.criar(dto));
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar lote existente")
    public ResponseEntity<LoteDTO> atualizar(
            @PathVariable String id,
            @Valid @RequestBody LoteDTO dto) {
        return ResponseEntity.ok(loteService.atualizar(id, dto));
    }
    
    @PatchMapping("/{id}/status")
    @Operation(summary = "Atualizar status do lote")
    public ResponseEntity<LoteDTO> atualizarStatus(
            @PathVariable String id,
            @RequestParam LoteStatus status) {
        return ResponseEntity.ok(loteService.atualizarStatus(id, status));
    }
    
    @PostMapping("/{id}/aprovar")
    @Operation(summary = "Aprovar lote", description = "Aprova um lote em quarentena, mudando seu status para APROVADO")
    public ResponseEntity<LoteDTO> aprovarLote(@PathVariable String id) {
        return ResponseEntity.ok(loteService.aprovarLote(id));
    }
    
    @PostMapping("/{id}/reprovar")
    @Operation(summary = "Reprovar lote", description = "Reprova um lote em quarentena, mudando seu status para REPROVADO")
    public ResponseEntity<LoteDTO> reprovarLote(
            @PathVariable String id,
            @RequestParam(required = false) String motivo) {
        return ResponseEntity.ok(loteService.reprovarLote(id, motivo));
    }
}