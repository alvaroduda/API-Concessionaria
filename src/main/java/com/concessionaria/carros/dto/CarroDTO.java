package com.concessionaria.carros.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarroDTO {
    private Long id;
    
    @NotBlank(message = "A marca é obrigatória")
    private String marca;
    
    @NotBlank(message = "O modelo é obrigatório")
    private String modelo;
    
    @NotNull(message = "O ano é obrigatório")
    @Positive(message = "O ano deve ser positivo")
    private Integer ano;
    
    @NotBlank(message = "A placa é obrigatória")
    private String placa;
    
    @NotNull(message = "O preço de venda é obrigatório")
    @Positive(message = "O preço de venda deve ser positivo")
    private BigDecimal precoVenda;
    
    @NotNull(message = "O preço de aluguel é obrigatório")
    @Positive(message = "O preço de aluguel deve ser positivo")
    private BigDecimal precoAluguel;
    
    private Boolean disponivel;
} 