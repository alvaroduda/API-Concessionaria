package com.concessionaria.carros.controller;

import com.concessionaria.carros.dto.OperacaoDTO;
import com.concessionaria.carros.entity.Operacao;
import com.concessionaria.carros.service.OperacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/operacoes")
@RequiredArgsConstructor
@Tag(name = "Operações", description = "APIs de gerenciamento de operações")
public class OperacaoController {

    private final OperacaoService operacaoService;

    @PostMapping
    @Operation(summary = "Criar nova operação")
    public ResponseEntity<OperacaoDTO> criar(@Valid @RequestBody OperacaoDTO operacao) {
        return ResponseEntity.ok(operacaoService.criar(operacao));
    }

    @GetMapping
    @Operation(summary = "Listar todas as operações")
    public ResponseEntity<List<OperacaoDTO>> listarTodos() {
        return ResponseEntity.ok(operacaoService.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar operação por ID")
    public ResponseEntity<OperacaoDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(operacaoService.buscarPorId(id));
    }

    @PostMapping("/{id}/finalizar")
    @Operation(summary = "Finalizar aluguel")
    public ResponseEntity<OperacaoDTO> finalizarAluguel(@PathVariable Long id) {
        return ResponseEntity.ok(operacaoService.finalizarAluguel(id));
    }

    @GetMapping("/carro/{carroId}")
    @Operation(summary = "Buscar operações por ID do carro")
    public ResponseEntity<List<OperacaoDTO>> buscarPorCarro(@PathVariable Long carroId) {
        return ResponseEntity.ok(operacaoService.buscarPorCarro(carroId));
    }

    @GetMapping("/cliente/{clienteId}")
    @Operation(summary = "Buscar operações por ID do cliente")
    public ResponseEntity<List<OperacaoDTO>> buscarPorCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(operacaoService.buscarPorCliente(clienteId));
    }

    @GetMapping("/tipo/{tipo}")
    @Operation(summary = "Buscar operações por tipo")
    public ResponseEntity<List<OperacaoDTO>> buscarPorTipo(@PathVariable Operacao.TipoOperacao tipo) {
        return ResponseEntity.ok(operacaoService.buscarPorTipo(tipo));
    }
}
