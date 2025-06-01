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
@Table(name = "operacoes")
public class Operacao {
    
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    
    @ManyToOne
    @JoinColumn(name = "carro_id", nullable = false)
    private Carro carro;
    
    
    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Usuario cliente;
    
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoOperacao tipo;
    
    
    @Column(nullable = false)
    private BigDecimal valor;
    
    
    @Column(nullable = false)
    private LocalDateTime dataOperacao;
    
    
    @Column
    private LocalDateTime dataDevolucao;
    
    
    @PrePersist
    protected void onCreate() {
        dataOperacao = LocalDateTime.now();
    }
    
    
    public enum TipoOperacao {
        VENDA,
        ALUGUEL
    }
} 