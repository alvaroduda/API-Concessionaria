package com.concessionaria.carros.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "carros")
public class Carro {
    

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    

    @Column(nullable = false)
    private String marca;
    
  
    @Column(nullable = false)
    private String modelo;
    
   
    @Column(nullable = false)
    private Integer ano;
    
   
    @Column(nullable = false, unique = true)
    private String placa;
    
   
    @Column(nullable = false)
    private BigDecimal precoVenda;
    
   
    @Column(nullable = false)
    private BigDecimal precoAluguel;
    
  
    @Column(nullable = false)
    private Boolean disponivel;
    
   
    @Column(nullable = false)
    private LocalDateTime dataCadastro;
    
    
    @PrePersist
    protected void onCreate() {
        dataCadastro = LocalDateTime.now();
        disponivel = true;
    }
} 