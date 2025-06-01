package com.concessionaria.carros.controller;

import com.concessionaria.carros.dto.CarroDTO;
import com.concessionaria.carros.service.CarroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/carros")
@RequiredArgsConstructor
@Tag(name = "Carros", description = "APIs de gerenciamento de carros")
public class CarroController {

    private final CarroService carroService;

    @PostMapping
    @Operation(summary = "Criar um novo carro")
    public ResponseEntity<CarroDTO> criar(@RequestBody CarroDTO dto) {
        CarroDTO carroCriado = carroService.criar(dto);
        return ResponseEntity.created(URI.create("/api/carros/" + carroCriado.getId()))
                .body(carroCriado);
    }

    @GetMapping
    @Operation(summary = "Listar todos os carros")
    public ResponseEntity<List<CarroDTO>> listarTodos() {
        return ResponseEntity.ok(carroService.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar carro por ID")
    public ResponseEntity<CarroDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(carroService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar um carro existente")
    public ResponseEntity<CarroDTO> atualizar(@PathVariable Long id, @RequestBody CarroDTO dto) {
        return ResponseEntity.ok(carroService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar um carro")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        carroService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/marca/{marca}")
    @Operation(summary = "Buscar carros por marca")
    public ResponseEntity<List<CarroDTO>> buscarPorMarca(@PathVariable String marca) {
        return ResponseEntity.ok(carroService.buscarPorMarca(marca));
    }

    @GetMapping("/marca/{marca}/modelo/{modelo}")
    @Operation(summary = "Buscar carro por marca e modelo")
    public ResponseEntity<CarroDTO> buscarPorMarcaEModelo(
            @PathVariable String marca,
            @PathVariable String modelo) {
        return ResponseEntity.ok(carroService.buscarPorMarcaEModelo(marca, modelo));
    }

    @GetMapping("/disponiveis")
    @Operation(summary = "Listar carros disponíveis")
    public ResponseEntity<List<CarroDTO>> buscarDisponiveis() {
        return ResponseEntity.ok(carroService.buscarDisponiveis());
    }

    @GetMapping("/placa/{placa}")
    @Operation(summary = "Buscar carro por placa")
    public ResponseEntity<CarroDTO> buscarPorPlaca(@PathVariable String placa) {
        return ResponseEntity.ok(carroService.buscarPorPlaca(placa));
    }
}
