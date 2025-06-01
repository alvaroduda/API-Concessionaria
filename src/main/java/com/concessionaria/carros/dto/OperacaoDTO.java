package com.concessionaria.carros.dto;

import com.concessionaria.carros.entity.Operacao;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OperacaoDTO {
    private Long id;
    
    @NotNull(message = "O ID do carro é obrigatório")
    private Long carroId;
    
    @NotNull(message = "O ID do cliente é obrigatório")
    private Long clienteId;
    
    @NotNull(message = "O tipo da operação é obrigatório")
    private Operacao.TipoOperacao tipo;
    
    @NotNull(message = "O valor é obrigatório")
    private BigDecimal valor;
    
    private LocalDateTime dataOperacao;
    private LocalDateTime dataDevolucao;
    
    private String marca;
} 