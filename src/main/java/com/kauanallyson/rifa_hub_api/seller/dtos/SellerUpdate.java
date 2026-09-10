package com.kauanallyson.rifa_hub_api.seller.dtos;

import jakarta.validation.constraints.Email;

public record SellerUpdate(
        String name,
        String phone,
        @Email(message = "Invalid email format")
        String email
) {
}
