package com.kauanallyson.rifa_hub_api.dtos.rifa;

import com.kauanallyson.rifa_hub_api.dtos.ponto.PontoResponseDTO;
import com.kauanallyson.rifa_hub_api.entities.enums.StatusRifa;

import java.util.List;

public record RifaDetailResponseDTO(
        Long id,
        String nome,
        String premio,
        StatusRifa status,
        List<PontoResponseDTO> pontos
) {
}