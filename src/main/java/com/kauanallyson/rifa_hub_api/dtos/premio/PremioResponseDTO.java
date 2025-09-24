package com.kauanallyson.rifa_hub_api.dtos.premio;

import com.kauanallyson.rifa_hub_api.dtos.ponto.PontoResponseDTO;

public record PremioResponseDTO(
        String descricao,
        Integer colocacao,
        PontoResponseDTO pontoVencedor
) {
}
