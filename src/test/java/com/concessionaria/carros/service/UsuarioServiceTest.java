package com.concessionaria.carros.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.concessionaria.carros.dto.UsuarioDTO;
import com.concessionaria.carros.entity.Usuario;
import com.concessionaria.carros.exception.BusinessException;
import com.concessionaria.carros.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;
    private UsuarioDTO usuarioDTO;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("João Silva");
        usuario.setEmail("joao@email.com");
        usuario.setSenha("123456");

        usuarioDTO = new UsuarioDTO();
        usuarioDTO.setId(1L);
        usuarioDTO.setNome("João Silva");
        usuarioDTO.setEmail("joao@email.com");
        usuarioDTO.setSenha("123456");
    }

    @Test
    void criar_DeveCriarUsuarioComSucesso() {
        when(usuarioRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        UsuarioDTO resultado = usuarioService.criar(usuarioDTO);

        assertNotNull(resultado);
        assertEquals(usuarioDTO.getNome(), resultado.getNome());
        assertEquals(usuarioDTO.getEmail(), resultado.getEmail());
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void criar_DeveLancarExcecaoQuandoEmailJaExiste() {
        when(usuarioRepository.findByEmail(anyString())).thenReturn(Optional.of(usuario));

        assertThrows(BusinessException.class, () -> usuarioService.criar(usuarioDTO));
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void atualizar_DeveAtualizarUsuarioComSucesso() {
        when(usuarioRepository.findById(anyLong())).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        UsuarioDTO resultado = usuarioService.atualizar(1L, usuarioDTO);

        assertNotNull(resultado);
        assertEquals(usuarioDTO.getNome(), resultado.getNome());
        assertEquals(usuarioDTO.getEmail(), resultado.getEmail());
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void atualizar_DeveLancarExcecaoQuandoUsuarioNaoEncontrado() {
        when(usuarioRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () -> usuarioService.atualizar(1L, usuarioDTO));
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void buscarPorId_DeveRetornarUsuarioQuandoEncontrado() {
        when(usuarioRepository.findById(anyLong())).thenReturn(Optional.of(usuario));

        UsuarioDTO resultado = usuarioService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals(usuario.getNome(), resultado.getNome());
        assertEquals(usuario.getEmail(), resultado.getEmail());
    }

    @Test
    void buscarPorId_DeveLancarExcecaoQuandoUsuarioNaoEncontrado() {
        when(usuarioRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () -> usuarioService.buscarPorId(1L));
    }

    @Test
    void listarTodos_DeveRetornarListaDeUsuarios() {
        List<Usuario> usuarios = Arrays.asList(usuario);
        when(usuarioRepository.findAll()).thenReturn(usuarios);

        List<UsuarioDTO> resultado = usuarioService.listarTodos();

        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertEquals(usuario.getNome(), resultado.get(0).getNome());
    }

    @Test
    void deletar_DeveDeletarUsuarioComSucesso() {
        when(usuarioRepository.findById(anyLong())).thenReturn(Optional.of(usuario));
        doNothing().when(usuarioRepository).delete(any(Usuario.class));

        usuarioService.deletar(1L);

        verify(usuarioRepository).delete(any(Usuario.class));
    }

    @Test
    void deletar_DeveLancarExcecaoQuandoUsuarioNaoEncontrado() {
        when(usuarioRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () -> usuarioService.deletar(1L));
        verify(usuarioRepository, never()).delete(any(Usuario.class));
    }
} 