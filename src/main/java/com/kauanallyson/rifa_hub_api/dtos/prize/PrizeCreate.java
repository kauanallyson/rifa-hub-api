package com.kauanallyson.rifa_hub_api.dtos.prize;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PrizeCreate(
        @NotBlank(message = "Description is required")
        String description,

        @NotNull(message = "Prize placement is required")
        @Positive(message = "Placing must be a positive number")
        Integer placement
) {
}