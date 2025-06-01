package com.concessionaria.carros.service;

import com.concessionaria.carros.dto.CarroDTO;
import com.concessionaria.carros.entity.Carro;
import com.concessionaria.carros.exception.BusinessException;
import com.concessionaria.carros.repository.CarroRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarroServiceTest {

    @Mock
    private CarroRepository carroRepository;

    @InjectMocks
    private CarroService carroService;

    private Carro carro;
    private CarroDTO carroDTO;

    @BeforeEach
    void setUp() {
        carro = new Carro();
        carro.setId(1L);
        carro.setMarca("Toyota");
        carro.setModelo("Corolla");
        carro.setAno(2020);
        carro.setPlaca("ABC1234");
        carro.setPrecoVenda(new BigDecimal("80000.00"));
        carro.setPrecoAluguel(new BigDecimal("200.00"));
        carro.setDisponivel(true);

        carroDTO = new CarroDTO();
        carroDTO.setId(1L);
        carroDTO.setMarca("Toyota");
        carroDTO.setModelo("Corolla");
        carroDTO.setAno(2020);
        carroDTO.setPlaca("ABC1234");
        carroDTO.setPrecoVenda(new BigDecimal("80000.00"));
        carroDTO.setPrecoAluguel(new BigDecimal("200.00"));
        carroDTO.setDisponivel(true);
    }

    @Test
    void criar_DeveCriarCarroComSucesso() {
        when(carroRepository.existsByMarcaAndModelo(anyString(), anyString())).thenReturn(false);
        when(carroRepository.save(any(Carro.class))).thenReturn(carro);

        CarroDTO resultado = carroService.criar(carroDTO);

        assertNotNull(resultado);
        assertEquals(carroDTO.getMarca(), resultado.getMarca());
        assertEquals(carroDTO.getModelo(), resultado.getModelo());
        verify(carroRepository).save(any(Carro.class));
    }

    @Test
    void criar_DeveLancarExcecaoQuandoMarcaEModeloJaExistem() {
        when(carroRepository.existsByMarcaAndModelo(anyString(), anyString())).thenReturn(true);

        assertThrows(BusinessException.class, () -> carroService.criar(carroDTO));
        verify(carroRepository, never()).save(any(Carro.class));
    }

    @Test
    void listarTodos_DeveRetornarListaDeCarros() {
        List<Carro> carros = Collections.singletonList(carro);
        when(carroRepository.findAll()).thenReturn(carros);

        List<CarroDTO> resultado = carroService.listarTodos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(carro.getMarca(), resultado.get(0).getMarca());
    }

    @Test
    void buscarPorId_DeveRetornarCarroQuandoEncontrado() {
        when(carroRepository.findById(1L)).thenReturn(Optional.of(carro));

        CarroDTO resultado = carroService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals(carro.getMarca(), resultado.getMarca());
    }

    @Test
    void buscarPorId_DeveLancarExcecaoQuandoCarroNaoEncontrado() {
        when(carroRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () -> carroService.buscarPorId(1L));
    }

    @Test
    void buscarPorMarca_DeveRetornarCarrosDaMarca() {
        List<Carro> carros = Collections.singletonList(carro);
        when(carroRepository.findByMarca(anyString())).thenReturn(carros);

        List<CarroDTO> resultado = carroService.buscarPorMarca("Toyota");

        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertEquals(carro.getMarca(), resultado.get(0).getMarca());
    }

    @Test
    void buscarPorMarcaEModelo_DeveRetornarCarro() {
        List<Carro> carros = Collections.singletonList(carro);
        when(carroRepository.findByMarcaAndModelo(anyString(), anyString())).thenReturn(carros);

        CarroDTO resultado = carroService.buscarPorMarcaEModelo("Toyota", "Corolla");

        assertNotNull(resultado);
        assertEquals(carro.getMarca(), resultado.getMarca());
        assertEquals(carro.getModelo(), resultado.getModelo());
    }

    @Test
    void buscarPorPlaca_DeveRetornarCarro() {
        when(carroRepository.findByPlaca(anyString())).thenReturn(Optional.of(carro));

        CarroDTO resultado = carroService.buscarPorPlaca("ABC1234");

        assertNotNull(resultado);
        assertEquals(carro.getPlaca(), resultado.getPlaca());
    }

    @Test
    void buscarPorPlaca_DeveLancarExcecaoQuandoCarroNaoEncontrado() {
        when(carroRepository.findByPlaca(anyString())).thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () -> carroService.buscarPorPlaca("ABC1234"));
    }

    @Test
    void buscarDisponiveis_DeveRetornarCarrosDisponiveis() {
        List<Carro> carros = Collections.singletonList(carro);
        when(carroRepository.findByDisponivelTrue()).thenReturn(carros);

        List<CarroDTO> resultado = carroService.buscarDisponiveis();

        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertTrue(resultado.get(0).getDisponivel());
    }

    @Test
    void atualizar_DeveAtualizarCarroComSucesso() {
        when(carroRepository.findById(anyLong())).thenReturn(Optional.of(carro));
        when(carroRepository.existsByMarcaAndModelo(anyString(), anyString())).thenReturn(false);
        when(carroRepository.save(any(Carro.class))).thenReturn(carro);

        CarroDTO resultado = carroService.atualizar(1L, carroDTO);

        assertNotNull(resultado);
        assertEquals(carroDTO.getMarca(), resultado.getMarca());
        assertEquals(carroDTO.getModelo(), resultado.getModelo());
        verify(carroRepository).save(any(Carro.class));
    }

    @Test
    void atualizar_DeveLancarExcecaoQuandoCarroNaoEncontrado() {
        when(carroRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () -> carroService.atualizar(1L, carroDTO));
        verify(carroRepository, never()).save(any(Carro.class));
    }

    @Test
    void atualizar_DeveLancarExcecaoQuandoMarcaEModeloJaExistemEmOutroCarro() {
        when(carroRepository.findById(anyLong())).thenReturn(Optional.of(carro));
        when(carroRepository.existsByMarcaAndModelo(anyString(), anyString())).thenReturn(true);

        assertThrows(BusinessException.class, () -> carroService.atualizar(1L, carroDTO));
        verify(carroRepository, never()).save(any(Carro.class));
    }

    @Test
    void deletar_DeveDeletarCarroComSucesso() {
        when(carroRepository.findById(anyLong())).thenReturn(Optional.of(carro));
        doNothing().when(carroRepository).delete(any(Carro.class));

        carroService.deletar(1L);

        verify(carroRepository).delete(any(Carro.class));
    }

    @Test
    void deletar_DeveLancarExcecaoQuandoCarroNaoEncontrado() {
        when(carroRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () -> carroService.deletar(1L));
        verify(carroRepository, never()).delete(any(Carro.class));
    }
} 