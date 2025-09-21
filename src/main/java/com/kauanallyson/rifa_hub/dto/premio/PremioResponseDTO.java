package com.kauanallyson.rifa_hub.dto.premio;

import com.kauanallyson.rifa_hub.dto.ponto.PontoResponseDTO;

public record PremioResponseDTO(
    String descricao,
    Integer colocacao,
    PontoResponseDTO pontoVencedor
) {}
