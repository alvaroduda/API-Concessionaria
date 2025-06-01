package com.concessionaria.carros.service;

import com.concessionaria.carros.dto.OperacaoDTO;
import com.concessionaria.carros.entity.Carro;
import com.concessionaria.carros.entity.Operacao;
import com.concessionaria.carros.entity.Usuario;
import com.concessionaria.carros.exception.BusinessException;
import com.concessionaria.carros.repository.CarroRepository;
import com.concessionaria.carros.repository.OperacaoRepository;
import com.concessionaria.carros.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service // Define esta classe como um componente de serviço do Spring.
@RequiredArgsConstructor // Gera automaticamente o construtor com os atributos 'final'.
public class OperacaoService {

    private static final Logger logger = LoggerFactory.getLogger(OperacaoService.class);
    private final OperacaoRepository operacaoRepository;
    private final CarroRepository carroRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public OperacaoDTO criar(OperacaoDTO dto) {
        logger.info("Criando nova operação: marca={}, clienteId={}, tipo={}", dto.getMarca(), dto.getClienteId(), dto.getTipo());
        
        List<Carro> carros = carroRepository.findByMarca(dto.getMarca());
        if (carros.isEmpty()) {
            logger.warn("Nenhum carro encontrado para a marca: {}", dto.getMarca());
            throw new BusinessException("Nenhum carro encontrado para a marca: " + dto.getMarca());
        }
        Carro carro = carros.get(0);

        Usuario cliente = usuarioRepository.findById(dto.getClienteId())
                .orElseThrow(() -> {
                    logger.warn("Cliente não encontrado: id={}", dto.getClienteId());
                    return new BusinessException("Cliente não encontrado");
                });

        validarOperacao(carro, dto.getTipo());

        Operacao operacao = new Operacao();
        operacao.setCarro(carro);
        operacao.setCliente(cliente);
        operacao.setTipo(dto.getTipo());
        operacao.setValor(dto.getValor());

        carro.setDisponivel(false);
        carroRepository.save(carro);

        operacao = operacaoRepository.save(operacao);
        logger.info("Operação criada com sucesso: id={}", operacao.getId());

        return converterParaDTO(operacao);
    }

    @Transactional(readOnly = true)
    public List<OperacaoDTO> listarTodos() {
        logger.info("Listando todas as operações");
        return operacaoRepository.findAll().stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public OperacaoDTO buscarPorId(Long id) {
        logger.info("Buscando operação por id: {}", id);
        return operacaoRepository.findById(id)
                .map(this::converterParaDTO)
                .orElseThrow(() -> {
                    logger.warn("Operação não encontrada: id={}", id);
                    return new BusinessException("Operação não encontrada");
                });
    }

    @Transactional
    public OperacaoDTO finalizarAluguel(Long id) {
        logger.info("Finalizando aluguel: id={}", id);
        
        Operacao operacao = operacaoRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Operação não encontrada para finalização: id={}", id);
                    return new BusinessException("Operação não encontrada");
                });

        if (operacao.getTipo() != Operacao.TipoOperacao.ALUGUEL) {
            logger.warn("Tentativa de finalizar operação que não é aluguel: id={}, tipo={}", id, operacao.getTipo());
            throw new BusinessException("Apenas aluguéis podem ser finalizados");
        }

        if (operacao.getDataDevolucao() != null) {
            logger.warn("Tentativa de finalizar aluguel já finalizado: id={}", id);
            throw new BusinessException("Este aluguel já foi finalizado");
        }

        operacao.setDataDevolucao(java.time.LocalDateTime.now());
        operacao.getCarro().setDisponivel(true);
        carroRepository.save(operacao.getCarro());

        operacao = operacaoRepository.save(operacao);
        logger.info("Aluguel finalizado com sucesso: id={}", operacao.getId());
        
        return converterParaDTO(operacao);
    }

    @Transactional(readOnly = true)
    public List<OperacaoDTO> buscarPorCarro(Long carroId) {
        logger.info("Buscando operações por carro: id={}", carroId);
        List<Operacao> operacoes = operacaoRepository.findByCarroId(carroId);
        if (operacoes.isEmpty()) {
            logger.warn("Nenhuma operação encontrada para o carro: id={}", carroId);
            throw new BusinessException("Nenhuma operação encontrada para o carro");
        }
        return operacoes.stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OperacaoDTO> buscarPorCliente(Long clienteId) {
        logger.info("Buscando operações por cliente: id={}", clienteId);
        List<Operacao> operacoes = operacaoRepository.findByClienteId(clienteId);
        if (operacoes.isEmpty()) {
            logger.warn("Nenhuma operação encontrada para o cliente: id={}", clienteId);
            throw new BusinessException("Nenhuma operação encontrada para o cliente");
        }
        return operacoes.stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OperacaoDTO> buscarPorTipo(Operacao.TipoOperacao tipo) {
        logger.info("Buscando operações por tipo: {}", tipo);
        List<Operacao> operacoes = operacaoRepository.findByTipo(tipo);
        if (operacoes.isEmpty()) {
            logger.warn("Nenhuma operação encontrada do tipo: {}", tipo);
            throw new BusinessException("Nenhuma operação encontrada do tipo: " + tipo);
        }
        return operacoes.stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    private void validarOperacao(Carro carro, Operacao.TipoOperacao tipo) {
        if (!carro.getDisponivel()) {
            logger.warn("Tentativa de operação com carro indisponível: id={}", carro.getId());
            throw new BusinessException("Carro não está disponível");
        }
    }

    private OperacaoDTO converterParaDTO(Operacao operacao) {
        OperacaoDTO dto = new OperacaoDTO();
        dto.setId(operacao.getId());
        dto.setMarca(operacao.getCarro().getMarca());
        dto.setClienteId(operacao.getCliente().getId());
        dto.setTipo(operacao.getTipo());
        dto.setValor(operacao.getValor());
        dto.setDataOperacao(operacao.getDataOperacao());
        dto.setDataDevolucao(operacao.getDataDevolucao());
        return dto;
    }
}
