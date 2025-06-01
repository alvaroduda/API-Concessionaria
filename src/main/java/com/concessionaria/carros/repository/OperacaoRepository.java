package com.concessionaria.carros.repository;

import com.concessionaria.carros.entity.Operacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OperacaoRepository extends JpaRepository<Operacao, Long> {
    List<Operacao> findByCarroId(Long carroId);
    List<Operacao> findByClienteId(Long clienteId);
    List<Operacao> findByTipo(Operacao.TipoOperacao tipo);
} 