package com.concessionaria.carros.controller;

import com.concessionaria.carros.dto.OperacaoDTO;
import com.concessionaria.carros.entity.Operacao;
import com.concessionaria.carros.service.OperacaoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OperacaoController.class)
class OperacaoControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @MockBean
    private OperacaoService operacaoService;
    
    private OperacaoDTO operacaoDTO;
    
    @BeforeEach
    void setUp() {
        operacaoDTO = new OperacaoDTO();
        operacaoDTO.setId(1L);
        operacaoDTO.setMarca("Toyota");
        operacaoDTO.setClienteId(1L);
        operacaoDTO.setTipo(Operacao.TipoOperacao.VENDA);
        operacaoDTO.setValor(new BigDecimal("50000.00"));
        operacaoDTO.setDataOperacao(LocalDateTime.now());
    }
    
    @Test
    void criar_DeveCriarOperacaoComSucesso() throws Exception {
        when(operacaoService.criar(any(OperacaoDTO.class))).thenReturn(operacaoDTO);
        
        mockMvc.perform(post("/api/operacoes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(operacaoDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.marca").value(operacaoDTO.getMarca()))
                .andExpect(jsonPath("$.clienteId").value(operacaoDTO.getClienteId()))
                .andExpect(jsonPath("$.tipo").value(operacaoDTO.getTipo().toString()));
                
        verify(operacaoService).criar(any(OperacaoDTO.class));
    }
    
    @Test
    void listarTodos_DeveRetornarListaDeOperacoes() throws Exception {
        List<OperacaoDTO> operacoes = Collections.singletonList(operacaoDTO);
        when(operacaoService.listarTodos()).thenReturn(operacoes);
        
        mockMvc.perform(get("/api/operacoes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].marca").value(operacaoDTO.getMarca()))
                .andExpect(jsonPath("$[0].clienteId").value(operacaoDTO.getClienteId()));
                
        verify(operacaoService).listarTodos();
    }
    
    @Test
    void buscarPorId_DeveRetornarOperacaoQuandoEncontrada() throws Exception {
        when(operacaoService.buscarPorId(anyLong())).thenReturn(operacaoDTO);
        
        mockMvc.perform(get("/api/operacoes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.marca").value(operacaoDTO.getMarca()))
                .andExpect(jsonPath("$.clienteId").value(operacaoDTO.getClienteId()));
                
        verify(operacaoService).buscarPorId(1L);
    }
    
    @Test
    void finalizarAluguel_DeveFinalizarAluguelComSucesso() throws Exception {
        operacaoDTO.setTipo(Operacao.TipoOperacao.ALUGUEL);
        operacaoDTO.setDataDevolucao(LocalDateTime.now());
        when(operacaoService.finalizarAluguel(anyLong())).thenReturn(operacaoDTO);
        
        mockMvc.perform(post("/api/operacoes/1/finalizar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tipo").value(Operacao.TipoOperacao.ALUGUEL.toString()))
                .andExpect(jsonPath("$.dataDevolucao").exists());
                
        verify(operacaoService).finalizarAluguel(1L);
    }
    
    @Test
    void buscarPorCarro_DeveRetornarOperacoesDoCarro() throws Exception {
        List<OperacaoDTO> operacoes = Arrays.asList(operacaoDTO);
        when(operacaoService.buscarPorCarro(anyLong())).thenReturn(operacoes);
        
        mockMvc.perform(get("/api/operacoes/carro/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].marca").value(operacaoDTO.getMarca()))
                .andExpect(jsonPath("$[0].clienteId").value(operacaoDTO.getClienteId()));
                
        verify(operacaoService).buscarPorCarro(1L);
    }
    
    @Test
    void buscarPorCliente_DeveRetornarOperacoesDoCliente() throws Exception {
        List<OperacaoDTO> operacoes = Arrays.asList(operacaoDTO);
        when(operacaoService.buscarPorCliente(anyLong())).thenReturn(operacoes);
        
        mockMvc.perform(get("/api/operacoes/cliente/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].marca").value(operacaoDTO.getMarca()))
                .andExpect(jsonPath("$[0].clienteId").value(operacaoDTO.getClienteId()));
                
        verify(operacaoService).buscarPorCliente(1L);
    }
    
    @Test
    void buscarPorTipo_DeveRetornarOperacoesDoTipo() throws Exception {
        List<OperacaoDTO> operacoes = Arrays.asList(operacaoDTO);
        when(operacaoService.buscarPorTipo(any(Operacao.TipoOperacao.class))).thenReturn(operacoes);
        
        mockMvc.perform(get("/api/operacoes/tipo/VENDA"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].marca").value(operacaoDTO.getMarca()))
                .andExpect(jsonPath("$[0].tipo").value(Operacao.TipoOperacao.VENDA.toString()));
                
        verify(operacaoService).buscarPorTipo(Operacao.TipoOperacao.VENDA);
    }
} 