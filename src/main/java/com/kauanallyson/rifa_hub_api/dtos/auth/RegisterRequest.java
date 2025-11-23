package com.kauanallyson.rifa_hub_api.dtos.auth;

import jakarta.validation.constraints.NotBlank;

public record RegisterRequest(
        @NotBlank(message = "Login cannot be empty")
        String login,
        @NotBlank(message = "Password cannot be empty")
        String password) {
}
