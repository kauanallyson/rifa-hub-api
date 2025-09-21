package com.kauanallyson.rifa_hub_api.dto.premio;

import com.kauanallyson.rifa_hub_api.dto.ponto.PontoResponseDTO;

public record PremioResponseDTO(
    String descricao,
    Integer colocacao,
    PontoResponseDTO pontoVencedor
) {}
