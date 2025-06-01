package com.concessionaria.carros.service;

import com.concessionaria.carros.dto.CarroDTO;
import com.concessionaria.carros.entity.Carro;
import com.concessionaria.carros.exception.BusinessException;
import com.concessionaria.carros.repository.CarroRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service 
@RequiredArgsConstructor 
public class CarroService {

    private static final Logger logger = LoggerFactory.getLogger(CarroService.class);
    private final CarroRepository carroRepository;

    @Transactional 
    public CarroDTO criar(CarroDTO dto) {
        logger.info("Criando novo carro: marca={}, modelo={}", dto.getMarca(), dto.getModelo());
        
        if (carroRepository.existsByMarcaAndModelo(dto.getMarca(), dto.getModelo())) {
            logger.warn("Tentativa de criar carro duplicado: marca={}, modelo={}", dto.getMarca(), dto.getModelo());
            throw new BusinessException("Já existe um carro com esta marca e modelo");
        }

        Carro carro = new Carro();
        carro.setMarca(dto.getMarca());
        carro.setModelo(dto.getModelo());
        carro.setAno(dto.getAno());
        carro.setPlaca(dto.getPlaca());
        carro.setPrecoVenda(dto.getPrecoVenda());
        carro.setPrecoAluguel(dto.getPrecoAluguel());
        carro.setDisponivel(true);

        carro = carroRepository.save(carro);
        logger.info("Carro criado com sucesso: id={}", carro.getId());
        return converterParaDTO(carro);
    }

    @Transactional(readOnly = true)
    public List<CarroDTO> listarTodos() {
        logger.info("Listando todos os carros");
        return carroRepository.findAll().stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CarroDTO buscarPorId(Long id) {
        logger.info("Buscando carro por id: {}", id);
        return carroRepository.findById(id)
                .map(this::converterParaDTO)
                .orElseThrow(() -> {
                    logger.warn("Carro não encontrado: id={}", id);
                    return new BusinessException("Carro não encontrado");
                });
    }

    @Transactional
    public CarroDTO atualizar(Long id, CarroDTO dto) {
        logger.info("Atualizando carro: id={}", id);
        
        Carro carro = carroRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Carro não encontrado para atualização: id={}", id);
                    return new BusinessException("Carro não encontrado");
                });

        if (!carro.getMarca().equals(dto.getMarca()) || !carro.getModelo().equals(dto.getModelo())) {
            if (carroRepository.existsByMarcaAndModelo(dto.getMarca(), dto.getModelo())) {
                logger.warn("Tentativa de atualizar para marca/modelo duplicado: marca={}, modelo={}", dto.getMarca(), dto.getModelo());
                throw new BusinessException("Já existe um carro com esta marca e modelo");
            }
        }

        carro.setMarca(dto.getMarca());
        carro.setModelo(dto.getModelo());
        carro.setAno(dto.getAno());
        carro.setPlaca(dto.getPlaca());
        carro.setPrecoVenda(dto.getPrecoVenda());
        carro.setPrecoAluguel(dto.getPrecoAluguel());

        carro = carroRepository.save(carro);
        logger.info("Carro atualizado com sucesso: id={}", carro.getId());
        return converterParaDTO(carro);
    }

    @Transactional
    public void deletar(Long id) {
        logger.info("Deletando carro: id={}", id);
        
        if (!carroRepository.existsById(id)) {
            logger.warn("Tentativa de deletar carro inexistente: id={}", id);
            throw new BusinessException("Carro não encontrado");
        }
        
        carroRepository.deleteById(id);
        logger.info("Carro deletado com sucesso: id={}", id);
    }

    @Transactional(readOnly = true)
    public List<CarroDTO> buscarPorMarca(String marca) {
        logger.info("Buscando carros por marca: {}", marca);
        List<Carro> carros = carroRepository.findByMarca(marca);
        if (carros.isEmpty()) {
            logger.warn("Nenhum carro encontrado para a marca: {}", marca);
            throw new BusinessException("Nenhum carro encontrado para a marca: " + marca);
        }

        return carros.stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CarroDTO buscarPorMarcaEModelo(String marca, String modelo) {
        logger.info("Buscando carro por marca e modelo: marca={}, modelo={}", marca, modelo);
        List<Carro> carros = carroRepository.findByMarcaAndModelo(marca, modelo);
        if (carros.isEmpty()) {
            logger.warn("Nenhum carro encontrado para marca={} e modelo={}", marca, modelo);
            throw new BusinessException("Nenhum carro encontrado para a marca e modelo especificados");
        }

        return converterParaDTO(carros.get(0));
    }

    @Transactional(readOnly = true)
    public List<CarroDTO> buscarDisponiveis() {
        logger.info("Buscando carros disponíveis");
        return carroRepository.findByDisponivelTrue().stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CarroDTO buscarPorPlaca(String placa) {
        logger.info("Buscando carro por placa: {}", placa);
        return carroRepository.findByPlaca(placa)
                .map(this::converterParaDTO)
                .orElseThrow(() -> {
                    logger.warn("Carro não encontrado com a placa: {}", placa);
                    return new BusinessException("Carro não encontrado com a placa: " + placa);
                });
    }

    private CarroDTO converterParaDTO(Carro carro) {
        CarroDTO dto = new CarroDTO();
        dto.setId(carro.getId());
        dto.setMarca(carro.getMarca());
        dto.setModelo(carro.getModelo());
        dto.setAno(carro.getAno());
        dto.setPlaca(carro.getPlaca());
        dto.setPrecoVenda(carro.getPrecoVenda());
        dto.setPrecoAluguel(carro.getPrecoAluguel());
        dto.setDisponivel(carro.getDisponivel());
        return dto;
    }
}
