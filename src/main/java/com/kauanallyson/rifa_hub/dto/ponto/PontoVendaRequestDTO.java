package com.kauanallyson.rifa_hub.dto.ponto;

import jakarta.validation.constraints.NotNull;

public record PontoVendaRequestDTO(
    @NotNull(message = "O ID do comprador é obrigatório")
    Long compradorId,

    @NotNull(message = "O ID do vendedor é obrigatório")
    Long vendedorId
) {}
