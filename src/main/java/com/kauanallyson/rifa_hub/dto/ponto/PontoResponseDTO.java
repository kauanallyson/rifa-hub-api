package com.kauanallyson.rifa_hub.dto.ponto;

import com.kauanallyson.rifa_hub.entities.enums.StatusPonto;

public record PontoResponseDTO(
    Integer numero,
    StatusPonto status,
    String nomeComprador,
    String nomeVendedor
) {}
