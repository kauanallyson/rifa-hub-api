package com.kauanallyson.rifa_hub.dto.comprador;

import com.kauanallyson.rifa_hub.dto.ponto.PontoResponseDTO;

import java.util.List;

public record CompradorResponseDTO(
    Long id,
    String nome,
    String telefone,
    String email,
    List<PontoResponseDTO> pontosComprados
) {}
