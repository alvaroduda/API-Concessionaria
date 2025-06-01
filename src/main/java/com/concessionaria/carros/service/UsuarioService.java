package com.concessionaria.carros.service;

import com.concessionaria.carros.dto.UsuarioDTO;
import com.concessionaria.carros.entity.Usuario;
import com.concessionaria.carros.exception.BusinessException;
import com.concessionaria.carros.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    
    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);
    private final UsuarioRepository usuarioRepository;
    
    @Transactional
    public UsuarioDTO criar(UsuarioDTO dto) {
        logger.info("Criando novo usuário: email={}", dto.getEmail());
        
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            logger.warn("Tentativa de criar usuário com email duplicado: {}", dto.getEmail());
            throw new BusinessException("Já existe um usuário com este email");
        }
        
        Usuario usuario = new Usuario();
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setSenha(dto.getSenha());
        
        usuario = usuarioRepository.save(usuario);
        logger.info("Usuário criado com sucesso: id={}", usuario.getId());
        return converterParaDTO(usuario);
    }
    
    @Transactional(readOnly = true)
    public List<UsuarioDTO> listarTodos() {
        logger.info("Listando todos os usuários");
        return usuarioRepository.findAll().stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public UsuarioDTO buscarPorId(Long id) {
        logger.info("Buscando usuário por id: {}", id);
        return usuarioRepository.findById(id)
                .map(this::converterParaDTO)
                .orElseThrow(() -> {
                    logger.warn("Usuário não encontrado: id={}", id);
                    return new BusinessException("Usuário não encontrado");
                });
    }
    
    @Transactional
    public UsuarioDTO atualizar(Long id, UsuarioDTO dto) {
        logger.info("Atualizando usuário: id={}", id);
        
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Usuário não encontrado para atualização: id={}", id);
                    return new BusinessException("Usuário não encontrado");
                });
        
        if (!usuario.getEmail().equals(dto.getEmail()) && 
            usuarioRepository.existsByEmail(dto.getEmail())) {
            logger.warn("Tentativa de atualizar para email duplicado: {}", dto.getEmail());
            throw new BusinessException("Já existe um usuário com este email");
        }
        
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        if (dto.getSenha() != null && !dto.getSenha().isEmpty()) {
            usuario.setSenha(dto.getSenha());
        }
        
        usuario = usuarioRepository.save(usuario);
        logger.info("Usuário atualizado com sucesso: id={}", usuario.getId());
        return converterParaDTO(usuario);
    }
    
    @Transactional
    public void deletar(Long id) {
        logger.info("Deletando usuário: id={}", id);
        
        if (!usuarioRepository.existsById(id)) {
            logger.warn("Tentativa de deletar usuário inexistente: id={}", id);
            throw new BusinessException("Usuário não encontrado");
        }
        
        usuarioRepository.deleteById(id);
        logger.info("Usuário deletado com sucesso: id={}", id);
    }
    
    private UsuarioDTO converterParaDTO(Usuario usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setNome(usuario.getNome());
        dto.setEmail(usuario.getEmail());
        return dto;
    }
} 