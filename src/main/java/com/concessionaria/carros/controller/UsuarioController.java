package com.concessionaria.carros.controller;

import com.concessionaria.carros.dto.UsuarioDTO;
import com.concessionaria.carros.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
@Tag(name = "Usuários", description = "APIs de gerenciamento de usuários")
public class UsuarioController {
    
    private final UsuarioService usuarioService;
    
    @PostMapping
    @Operation(summary = "Criar novo usuário")
    public ResponseEntity<UsuarioDTO> criar(@Valid @RequestBody UsuarioDTO usuario) {
        return ResponseEntity.ok(usuarioService.criar(usuario));
    }
    
    @GetMapping
    @Operation(summary = "Listar todos os usuários")
    public ResponseEntity<List<UsuarioDTO>> listarTodos() {
        return ResponseEntity.ok(usuarioService.listarTodos());
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Buscar usuário por ID")
    public ResponseEntity<UsuarioDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.buscarPorId(id));
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar usuário")
    public ResponseEntity<UsuarioDTO> atualizar(@PathVariable Long id, @Valid @RequestBody UsuarioDTO usuario) {
        return ResponseEntity.ok(usuarioService.atualizar(id, usuario));
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Remover usuário")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        usuarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }
} 