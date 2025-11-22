package com.kauanallyson.rifa_hub_api.dtos.seller;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SellerCreate(
        @NotBlank(message = "Seller name is required")
        String name,

        @NotBlank(message = "Seller phone is required")
        String phone,

        @Email(message = "Invalid email format")
        String email
) {
}
