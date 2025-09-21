package com.kauanallyson.rifa_hub_api.dto.rifa;

import com.kauanallyson.rifa_hub_api.dto.ponto.PontoResponseDTO;
import com.kauanallyson.rifa_hub_api.entities.enums.StatusRifa;

import java.util.List;

public record RifaDetailResponseDTO(
        Long id,
        String nome,
        String premio,
        StatusRifa status,
        List<PontoResponseDTO> pontos
) {}