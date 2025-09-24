package com.kauanallyson.rifa_hub_api.dtos.vendedor;

import com.kauanallyson.rifa_hub_api.dtos.ponto.PontoResponseDTO;

import java.util.List;

public record VendedorResponseDTO(
        Long id,
        String nome,
        String telefone,
        String email,
        List<PontoResponseDTO> pontosVendidos
) {
}
