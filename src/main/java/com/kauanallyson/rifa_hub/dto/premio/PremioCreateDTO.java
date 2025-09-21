package com.kauanallyson.rifa_hub.dto.premio;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PremioCreateDTO(
    @NotBlank(message = "A descrição do prêmio é obrigatória")
    String descricao,

    @NotNull(message = "A colocação do prêmio é obrigatória")
    @Positive(message = "A colocação deve ser um número positivo")
    Integer colocacao
    ) {}