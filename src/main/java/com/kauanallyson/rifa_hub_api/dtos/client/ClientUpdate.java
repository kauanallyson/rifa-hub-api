package com.kauanallyson.rifa_hub_api.dtos.client;

import jakarta.validation.constraints.Email;

public record ClientUpdate(
        String name,
        String phone,
        @Email(message = "Invalid email format")
        String email
) {
}
