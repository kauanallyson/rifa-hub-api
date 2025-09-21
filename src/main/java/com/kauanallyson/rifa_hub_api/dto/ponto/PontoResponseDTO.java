package com.kauanallyson.rifa_hub_api.dto.ponto;

import com.kauanallyson.rifa_hub_api.entities.enums.StatusPonto;

public record PontoResponseDTO(
    Integer numero,
    StatusPonto status,
    String nomeComprador,
    String nomeVendedor
) {}
