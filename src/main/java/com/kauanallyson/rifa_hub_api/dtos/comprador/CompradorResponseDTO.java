package com.kauanallyson.rifa_hub_api.dtos.comprador;

import com.kauanallyson.rifa_hub_api.dtos.ponto.PontoResponseDTO;

import java.util.List;

public record CompradorResponseDTO(
        Long id,
        String nome,
        String telefone,
        String email,
        List<PontoResponseDTO> pontosComprados
) {
}
