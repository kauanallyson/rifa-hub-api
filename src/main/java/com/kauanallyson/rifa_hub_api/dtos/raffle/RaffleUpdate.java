package com.kauanallyson.rifa_hub_api.dtos.raffle;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record RaffleUpdate(
        String name,
        String description,
        @Positive(message = "Ticket price must be positive")
        BigDecimal ticketPrice,
        @Future(message = "Draw must happen on the future")
        LocalDateTime drawDate
) {
}