package com.concessionaria.carros.service;

import com.concessionaria.carros.dto.OperacaoDTO;
import com.concessionaria.carros.entity.Carro;
import com.concessionaria.carros.entity.Operacao;
import com.concessionaria.carros.entity.Usuario;
import com.concessionaria.carros.exception.BusinessException;
import com.concessionaria.carros.repository.CarroRepository;
import com.concessionaria.carros.repository.OperacaoRepository;
import com.concessionaria.carros.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OperacaoServiceTest {
    
    @Mock
    private OperacaoRepository operacaoRepository;
    
    @Mock
    private CarroRepository carroRepository;
    
    @Mock
    private UsuarioRepository usuarioRepository;
    
    @InjectMocks
    private OperacaoService operacaoService;
    
    private OperacaoDTO operacaoDTO;
    private Operacao operacao;
    private Carro carro;
    private Usuario usuario;
    
    @BeforeEach
    void setUp() {
        carro = new Carro();
        carro.setId(1L);
        carro.setMarca("Toyota");
        carro.setModelo("Corolla");
        carro.setPrecoVenda(new BigDecimal("150000.00"));
        carro.setPrecoAluguel(new BigDecimal("500.00"));
        carro.setDisponivel(true);
        
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("João Silva");
        usuario.setEmail("joao@email.com");
        
        operacaoDTO = new OperacaoDTO();
        operacaoDTO.setMarca("Toyota");
        operacaoDTO.setClienteId(1L);
        operacaoDTO.setTipo(Operacao.TipoOperacao.VENDA);
        
        operacao = new Operacao();
        operacao.setId(1L);
        operacao.setCarro(carro);
        operacao.setCliente(usuario);
        operacao.setTipo(Operacao.TipoOperacao.VENDA);
        operacao.setValor(new BigDecimal("150000.00"));
        operacao.setDataOperacao(LocalDateTime.now());
    }
    
    @Test
    void criar_DeveCriarOperacaoComSucesso() {
        when(carroRepository.findByMarca("Toyota")).thenReturn(Arrays.asList(carro));
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(operacaoRepository.save(any(Operacao.class))).thenReturn(operacao);
        
        OperacaoDTO resultado = operacaoService.criar(operacaoDTO);
        
        assertNotNull(resultado);
        assertEquals(operacaoDTO.getTipo(), resultado.getTipo());
        assertEquals(operacaoDTO.getMarca(), resultado.getMarca());
        verify(operacaoRepository).save(any(Operacao.class));
    }
    
    @Test
    void criar_DeveLancarExcecaoQuandoCarroNaoEncontrado() {
        when(carroRepository.findByMarca("Toyota")).thenReturn(Arrays.asList());
        
        assertThrows(BusinessException.class, () -> operacaoService.criar(operacaoDTO));
        verify(operacaoRepository, never()).save(any(Operacao.class));
    }
    
    @Test
    void criar_DeveLancarExcecaoQuandoClienteNaoEncontrado() {
        when(carroRepository.findByMarca("Toyota")).thenReturn(Arrays.asList(carro));
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());
        
        assertThrows(BusinessException.class, () -> operacaoService.criar(operacaoDTO));
        verify(operacaoRepository, never()).save(any(Operacao.class));
    }
    
    @Test
    void criar_DeveLancarExcecaoQuandoCarroNaoDisponivel() {
        carro.setDisponivel(false);
        when(carroRepository.findByMarca("Toyota")).thenReturn(Arrays.asList(carro));
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        
        assertThrows(BusinessException.class, () -> operacaoService.criar(operacaoDTO));
        verify(operacaoRepository, never()).save(any(Operacao.class));
    }
    
    @Test
    void listarTodos_DeveRetornarListaDeOperacoes() {
        List<Operacao> operacoes = Arrays.asList(operacao);
        when(operacaoRepository.findAll()).thenReturn(operacoes);
        
        List<OperacaoDTO> resultado = operacaoService.listarTodos();
        
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(operacao.getTipo(), resultado.get(0).getTipo());
    }
    
    @Test
    void buscarPorId_DeveRetornarOperacaoQuandoEncontrada() {
        when(operacaoRepository.findById(1L)).thenReturn(Optional.of(operacao));
        
        OperacaoDTO resultado = operacaoService.buscarPorId(1L);
        
        assertNotNull(resultado);
        assertEquals(operacao.getTipo(), resultado.getTipo());
        assertEquals(operacao.getCarro().getMarca(), resultado.getMarca());
    }
    
    @Test
    void buscarPorId_DeveLancarExcecaoQuandoOperacaoNaoEncontrada() {
        when(operacaoRepository.findById(1L)).thenReturn(Optional.empty());
        
        assertThrows(BusinessException.class, () -> operacaoService.buscarPorId(1L));
    }
    
    @Test
    void finalizarAluguel_DeveFinalizarAluguelComSucesso() {
        operacao.setTipo(Operacao.TipoOperacao.ALUGUEL);
        when(operacaoRepository.findById(1L)).thenReturn(Optional.of(operacao));
        when(operacaoRepository.save(any(Operacao.class))).thenReturn(operacao);
        
        OperacaoDTO resultado = operacaoService.finalizarAluguel(1L);
        
        assertNotNull(resultado);
        assertNotNull(resultado.getDataDevolucao());
        assertTrue(operacao.getCarro().getDisponivel());
        verify(operacaoRepository).save(any(Operacao.class));
    }
    
    @Test
    void finalizarAluguel_DeveLancarExcecaoQuandoOperacaoNaoEncontrada() {
        when(operacaoRepository.findById(1L)).thenReturn(Optional.empty());
        
        assertThrows(BusinessException.class, () -> operacaoService.finalizarAluguel(1L));
    }
    
    @Test
    void finalizarAluguel_DeveLancarExcecaoQuandoOperacaoNaoEhAluguel() {
        when(operacaoRepository.findById(1L)).thenReturn(Optional.of(operacao));
        
        assertThrows(BusinessException.class, () -> operacaoService.finalizarAluguel(1L));
    }
    
    @Test
    void finalizarAluguel_DeveLancarExcecaoQuandoAluguelJaFinalizado() {
        operacao.setTipo(Operacao.TipoOperacao.ALUGUEL);
        operacao.setDataDevolucao(LocalDateTime.now());
        when(operacaoRepository.findById(1L)).thenReturn(Optional.of(operacao));
        
        assertThrows(BusinessException.class, () -> operacaoService.finalizarAluguel(1L));
    }
    
    @Test
    void buscarPorCarro_DeveRetornarOperacoesDoCarro() {
        List<Operacao> operacoes = Arrays.asList(operacao);
        when(operacaoRepository.findByCarroId(1L)).thenReturn(operacoes);
        
        List<OperacaoDTO> resultado = operacaoService.buscarPorCarro(1L);
        
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(operacao.getCarro().getMarca(), resultado.get(0).getMarca());
    }
    
    @Test
    void buscarPorCarro_DeveLancarExcecaoQuandoNenhumaOperacaoEncontrada() {
        when(operacaoRepository.findByCarroId(1L)).thenReturn(Arrays.asList());
        
        assertThrows(BusinessException.class, () -> operacaoService.buscarPorCarro(1L));
    }
    
    @Test
    void buscarPorCliente_DeveRetornarOperacoesDoCliente() {
        List<Operacao> operacoes = Arrays.asList(operacao);
        when(operacaoRepository.findByClienteId(1L)).thenReturn(operacoes);
        
        List<OperacaoDTO> resultado = operacaoService.buscarPorCliente(1L);
        
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(operacao.getCliente().getId(), resultado.get(0).getClienteId());
    }
    
    @Test
    void buscarPorCliente_DeveLancarExcecaoQuandoNenhumaOperacaoEncontrada() {
        when(operacaoRepository.findByClienteId(1L)).thenReturn(Arrays.asList());
        
        assertThrows(BusinessException.class, () -> operacaoService.buscarPorCliente(1L));
    }
    
    @Test
    void buscarPorTipo_DeveRetornarOperacoesDoTipo() {
        List<Operacao> operacoes = Arrays.asList(operacao);
        when(operacaoRepository.findByTipo(Operacao.TipoOperacao.VENDA)).thenReturn(operacoes);
        
        List<OperacaoDTO> resultado = operacaoService.buscarPorTipo(Operacao.TipoOperacao.VENDA);
        
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(operacao.getTipo(), resultado.get(0).getTipo());
    }
    
    @Test
    void buscarPorTipo_DeveLancarExcecaoQuandoNenhumaOperacaoEncontrada() {
        when(operacaoRepository.findByTipo(Operacao.TipoOperacao.VENDA)).thenReturn(Arrays.asList());
        
        assertThrows(BusinessException.class, () -> operacaoService.buscarPorTipo(Operacao.TipoOperacao.VENDA));
    }
} 