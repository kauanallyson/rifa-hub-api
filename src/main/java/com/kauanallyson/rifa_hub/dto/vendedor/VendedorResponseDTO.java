package com.kauanallyson.rifa_hub.dto.vendedor;

import com.kauanallyson.rifa_hub.dto.ponto.PontoResponseDTO;

import java.util.List;

public record VendedorResponseDTO(
    Long id,
    String nome,
    String telefone,
    String email,
    List<PontoResponseDTO> pontosVendidos
) {}
