package com.kauanallyson.rifa_hub_api.dtos.client;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ClientCreateDTO(
        @NotBlank(message = "Client name is required")
        String name,

        @NotBlank(message = "Client phone is required")
        String phone,

        @Email(message = "Invalid email format")
        String email
) {
}
