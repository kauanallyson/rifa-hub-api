package com.kauanallyson.rifa_hub.dto.rifa;

import com.kauanallyson.rifa_hub.dto.premio.PremioCreateDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record RifaCreateDTO(
    @NotBlank(message = "O nome da rifa é obrigatório")
    String nome,

    String descricao,

    @NotNull(message = "O preço por ponto é obrigatório")
    @Positive(message = "O preço por ponto deve ser positivo")
    BigDecimal pontoPreco,

    @NotNull(message = "A quantidade de pontos é obrigatória")
    @Min(value = 10, message = "A rifa deve ter no mínimo 10 pontos")
    Integer quantidadePontos,

    @Future(message = "O sorteio deve acontecer no futuro")
    LocalDateTime dataSorteio,

    @Valid
    @NotEmpty
    List<PremioCreateDTO> premios
) {}
