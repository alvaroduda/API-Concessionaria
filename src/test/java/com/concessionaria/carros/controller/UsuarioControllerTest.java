package com.concessionaria.carros.controller;

import com.concessionaria.carros.dto.UsuarioDTO;
import com.concessionaria.carros.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsuarioController.class)
class UsuarioControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @MockBean
    private UsuarioService usuarioService;
    
    private UsuarioDTO usuarioDTO;
    
    @BeforeEach
    void setUp() {
        usuarioDTO = new UsuarioDTO();
        usuarioDTO.setId(1L);
        usuarioDTO.setNome("João Silva");
        usuarioDTO.setEmail("joao@email.com");
        usuarioDTO.setSenha("senha123");
    }
    
    @Test
    void criar_DeveCriarUsuarioComSucesso() throws Exception {
        when(usuarioService.criar(any(UsuarioDTO.class))).thenReturn(usuarioDTO);
        
        mockMvc.perform(post("/api/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuarioDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value(usuarioDTO.getNome()))
                .andExpect(jsonPath("$.email").value(usuarioDTO.getEmail()));
                
        verify(usuarioService).criar(any(UsuarioDTO.class));
    }
    
    @Test
    void listarTodos_DeveRetornarListaDeUsuarios() throws Exception {
        List<UsuarioDTO> usuarios = Collections.singletonList(usuarioDTO);
        when(usuarioService.listarTodos()).thenReturn(usuarios);
        
        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value(usuarioDTO.getNome()))
                .andExpect(jsonPath("$[0].email").value(usuarioDTO.getEmail()));
                
        verify(usuarioService).listarTodos();
    }
    
    @Test
    void buscarPorId_DeveRetornarUsuarioQuandoEncontrado() throws Exception {
        when(usuarioService.buscarPorId(anyLong())).thenReturn(usuarioDTO);
        
        mockMvc.perform(get("/api/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value(usuarioDTO.getNome()))
                .andExpect(jsonPath("$.email").value(usuarioDTO.getEmail()));
                
        verify(usuarioService).buscarPorId(1L);
    }
    
    @Test
    void atualizar_DeveAtualizarUsuarioComSucesso() throws Exception {
        when(usuarioService.atualizar(anyLong(), any(UsuarioDTO.class))).thenReturn(usuarioDTO);
        
        mockMvc.perform(put("/api/usuarios/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuarioDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value(usuarioDTO.getNome()))
                .andExpect(jsonPath("$.email").value(usuarioDTO.getEmail()));
                
        verify(usuarioService).atualizar(eq(1L), any(UsuarioDTO.class));
    }
    
    @Test
    void deletar_DeveDeletarUsuarioComSucesso() throws Exception {
        doNothing().when(usuarioService).deletar(anyLong());
        
        mockMvc.perform(delete("/api/usuarios/1"))
                .andExpect(status().isNoContent());
                
        verify(usuarioService).deletar(1L);
    }
} 