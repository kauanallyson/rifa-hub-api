package com.kauanallyson.rifa_hub_api.dto.rifa;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record RifaUpdateDTO(
        @NotBlank(message = "O nome da rifa é obrigatório")
        String nome,

        String descricao,

        @NotNull(message = "O preço por ponto é obrigatório")
        @Positive
        BigDecimal pontoPreco,

        @Future
        LocalDateTime dataSorteio
) {}