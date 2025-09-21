package com.kauanallyson.rifa_hub.dto.rifa;

import com.kauanallyson.rifa_hub.dto.ponto.PontoResponseDTO;
import com.kauanallyson.rifa_hub.entities.enums.StatusRifa;

import java.util.List;

public record RifaDetailResponseDTO(
        Long id,
        String nome,
        String premio,
        StatusRifa status,
        List<PontoResponseDTO> pontos
) {}