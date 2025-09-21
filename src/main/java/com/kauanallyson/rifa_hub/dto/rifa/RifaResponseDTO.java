package com.kauanallyson.rifa_hub.dto.rifa;

import com.kauanallyson.rifa_hub.dto.premio.PremioResponseDTO;
import com.kauanallyson.rifa_hub.entities.enums.StatusRifa;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record RifaResponseDTO(
    Long id,
    String nome,
    String descricao,
    List<PremioResponseDTO> premios,
    BigDecimal pontoPreco,
    Integer quantidadePontos,
    LocalDateTime dataSorteio,
    Long pontosVendidos,
    Long pontosDisponiveis,
    StatusRifa status
) {}
