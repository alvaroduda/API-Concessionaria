package com.concessionaria.carros.repository;

import com.concessionaria.carros.entity.Carro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarroRepository extends JpaRepository<Carro, Long> {
    Optional<Carro> findByPlaca(String placa);
    List<Carro> findByDisponivelTrue();
    List<Carro> findByMarcaAndModelo(String marca, String modelo);
    List<Carro> findByMarca(String marca);
    boolean existsByMarcaAndModelo(String marca, String modelo);
} 