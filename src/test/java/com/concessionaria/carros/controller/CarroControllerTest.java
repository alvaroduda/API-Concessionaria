package com.concessionaria.carros.controller;

import com.concessionaria.carros.dto.CarroDTO;
import com.concessionaria.carros.exception.BusinessException;
import com.concessionaria.carros.service.CarroService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CarroController.class)
class CarroControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @MockBean
    private CarroService carroService;
    
    private CarroDTO carroDTO;
    
    @BeforeEach
    void setUp() {
        carroDTO = new CarroDTO();
        carroDTO.setId(1L);
        carroDTO.setMarca("Toyota");
        carroDTO.setModelo("Corolla");
        carroDTO.setAno(2023);
        carroDTO.setPlaca("ABC1234");
        carroDTO.setPrecoVenda(new BigDecimal("150000.00"));
        carroDTO.setPrecoAluguel(new BigDecimal("500.00"));
        carroDTO.setDisponivel(true);
    }
    
    @Test
    void criar_DeveCriarCarroComSucesso() throws Exception {
        when(carroService.criar(any(CarroDTO.class))).thenReturn(carroDTO);
        
        mockMvc.perform(post("/api/carros")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(carroDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.marca").value(carroDTO.getMarca()))
                .andExpect(jsonPath("$.modelo").value(carroDTO.getModelo()))
                .andExpect(jsonPath("$.ano").value(carroDTO.getAno()));
                
        verify(carroService).criar(any(CarroDTO.class));
    }
    
    @Test
    void listarTodos_DeveRetornarListaDeCarros() throws Exception {
        List<CarroDTO> carros = Arrays.asList(carroDTO);
        when(carroService.listarTodos()).thenReturn(carros);
        
        mockMvc.perform(get("/api/carros"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].marca").value(carroDTO.getMarca()))
                .andExpect(jsonPath("$[0].modelo").value(carroDTO.getModelo()));
                
        verify(carroService).listarTodos();
    }
    
    @Test
    void buscarPorId_DeveRetornarCarroQuandoEncontrado() throws Exception {
        when(carroService.buscarPorId(anyLong())).thenReturn(carroDTO);
        
        mockMvc.perform(get("/api/carros/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.marca").value(carroDTO.getMarca()))
                .andExpect(jsonPath("$.modelo").value(carroDTO.getModelo()));
                
        verify(carroService).buscarPorId(1L);
    }
    
    @Test
    void atualizar_DeveAtualizarCarroComSucesso() throws Exception {
        when(carroService.atualizar(anyLong(), any(CarroDTO.class))).thenReturn(carroDTO);
        
        mockMvc.perform(put("/api/carros/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(carroDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.marca").value(carroDTO.getMarca()))
                .andExpect(jsonPath("$.modelo").value(carroDTO.getModelo()));
                
        verify(carroService).atualizar(eq(1L), any(CarroDTO.class));
    }
    
    @Test
    void deletar_DeveDeletarCarroComSucesso() throws Exception {
        doNothing().when(carroService).deletar(anyLong());
        
        mockMvc.perform(delete("/api/carros/1"))
                .andExpect(status().isNoContent());
                
        verify(carroService).deletar(1L);
    }
    
    @Test
    void buscarPorMarca_DeveRetornarCarrosDaMarca() throws Exception {
        List<CarroDTO> carros = Arrays.asList(carroDTO);
        when(carroService.buscarPorMarca(anyString())).thenReturn(carros);
        
        mockMvc.perform(get("/api/carros/marca/Toyota"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].marca").value(carroDTO.getMarca()))
                .andExpect(jsonPath("$[0].modelo").value(carroDTO.getModelo()));
                
        verify(carroService).buscarPorMarca("Toyota");
    }
    
    @Test
    void buscarPorMarcaEModelo_DeveRetornarCarro() throws Exception {
        when(carroService.buscarPorMarcaEModelo(anyString(), anyString())).thenReturn(carroDTO);
        
        mockMvc.perform(get("/api/carros/marca/Toyota/modelo/Corolla"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.marca").value(carroDTO.getMarca()))
                .andExpect(jsonPath("$.modelo").value(carroDTO.getModelo()));
                
        verify(carroService).buscarPorMarcaEModelo("Toyota", "Corolla");
    }
    
    @Test
    void buscarDisponiveis_DeveRetornarCarrosDisponiveis() throws Exception {
        List<CarroDTO> carros = Collections.singletonList(carroDTO);
        when(carroService.buscarDisponiveis()).thenReturn(carros);
        
        mockMvc.perform(get("/api/carros/disponiveis"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].marca").value(carroDTO.getMarca()))
                .andExpect(jsonPath("$[0].modelo").value(carroDTO.getModelo()))
                .andExpect(jsonPath("$[0].disponivel").value(true));
                
        verify(carroService).buscarDisponiveis();
    }

    @Test
    void buscarPorPlaca_DeveRetornarCarro() throws Exception {
        when(carroService.buscarPorPlaca(anyString())).thenReturn(carroDTO);
        
        mockMvc.perform(get("/api/carros/placa/ABC1234"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.marca").value(carroDTO.getMarca()))
                .andExpect(jsonPath("$.modelo").value(carroDTO.getModelo()));
                
        verify(carroService).buscarPorPlaca("ABC1234");
    }

    @Test
    void buscarPorPlaca_DeveRetornar404QuandoCarroNaoEncontrado() throws Exception {
        when(carroService.buscarPorPlaca(anyString()))
                .thenThrow(new BusinessException("Carro não encontrado com a placa: ABC1234"));
        
        mockMvc.perform(get("/api/carros/placa/ABC1234"))
                .andExpect(status().isNotFound());
                
        verify(carroService).buscarPorPlaca("ABC1234");
    }
} 