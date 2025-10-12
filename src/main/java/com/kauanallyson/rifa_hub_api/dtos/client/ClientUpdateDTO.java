package com.kauanallyson.rifa_hub_api.dtos.client;

import jakarta.validation.constraints.Email;

public record ClientUpdateDTO(
        String name,
        String phone,
        @Email(message = "Invalid email format")
        String email
) {
}
