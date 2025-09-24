package com.kauanallyson.rifa_hub_api.dtos.rifa;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record RifaUpdateDTO(
        String nome,
        String descricao,
        @Positive BigDecimal pontoPreco,
        @Future LocalDateTime dataSorteio
) {
}