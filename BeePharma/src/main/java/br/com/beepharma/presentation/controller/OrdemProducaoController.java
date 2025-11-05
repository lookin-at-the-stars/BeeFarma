package br.com.beepharma.presentation.controller;

import br.com.beepharma.application.dto.OrdemProducaoDTO;
import br.com.beepharma.application.service.OrdemProducaoService;
import br.com.beepharma.domain.enums.OPStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ordens-producao")
@RequiredArgsConstructor
@Tag(name = "Ordens de Produção", description = "API para gerenciamento de ordens de produção")
public class OrdemProducaoController {
    
    private final OrdemProducaoService ordemProducaoService;
    
    @GetMapping
    @Operation(summary = "Listar todas as ordens de produção")
    public ResponseEntity<List<OrdemProducaoDTO>> listarTodas() {
        return ResponseEntity.ok(ordemProducaoService.listarTodas());
    }
    
    @GetMapping("/status/{status}")
    @Operation(summary = "Listar ordens de produção por status")
    public ResponseEntity<List<OrdemProducaoDTO>> listarPorStatus(@PathVariable OPStatus status) {
        return ResponseEntity.ok(ordemProducaoService.listarPorStatus(status));
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Buscar ordem de produção por ID")
    public ResponseEntity<OrdemProducaoDTO> buscarPorId(@PathVariable String id) {
        return ResponseEntity.ok(ordemProducaoService.buscarPorId(id));
    }
    
    @PostMapping
    @Operation(summary = "Criar nova ordem de produção")
    public ResponseEntity<OrdemProducaoDTO> criar(@Valid @RequestBody OrdemProducaoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ordemProducaoService.criar(dto));
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar ordem de produção existente")
    public ResponseEntity<OrdemProducaoDTO> atualizar(
            @PathVariable String id,
            @Valid @RequestBody OrdemProducaoDTO dto) {
        return ResponseEntity.ok(ordemProducaoService.atualizar(id, dto));
    }
    
    @PatchMapping("/{id}/status")
    @Operation(summary = "Atualizar status da ordem de produção")
    public ResponseEntity<OrdemProducaoDTO> atualizarStatus(
            @PathVariable String id,
            @RequestParam OPStatus status) {
        return ResponseEntity.ok(ordemProducaoService.atualizarStatus(id, status));
    }
}