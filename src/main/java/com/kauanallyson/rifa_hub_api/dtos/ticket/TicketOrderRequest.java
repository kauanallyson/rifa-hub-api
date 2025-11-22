package com.kauanallyson.rifa_hub_api.dtos.ticket;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record TicketOrderRequest(
        @NotNull(message = "At least 1 ticket must be ordered")
        List<Integer> numbers,

        @NotNull(message = "Client id is required")
        Long clientId,

        @NotNull(message = "Seller id is required")
        Long sellerId
) {
}
