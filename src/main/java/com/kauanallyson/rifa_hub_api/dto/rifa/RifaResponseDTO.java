package com.kauanallyson.rifa_hub_api.dto.rifa;

import com.kauanallyson.rifa_hub_api.dto.premio.PremioResponseDTO;
import com.kauanallyson.rifa_hub_api.entities.enums.StatusRifa;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record RifaResponseDTO(
    Long id,
    String nome,
    String descricao,
    List<PremioResponseDTO> premios,
    BigDecimal pontoPreco,
    LocalDateTime dataSorteio,
    Integer quantidadePontos,
    Long pontosVendidos,
    Long pontosDisponiveis,
    StatusRifa status
) {}
