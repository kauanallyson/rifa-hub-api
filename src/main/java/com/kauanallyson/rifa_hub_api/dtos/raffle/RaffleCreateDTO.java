package com.kauanallyson.rifa_hub_api.dtos.raffle;

import com.kauanallyson.rifa_hub_api.dtos.prize.PrizeCreateDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record RaffleCreateDTO(
        @NotBlank(message = "Raffle name is required")
        String name,

        String description,

        @NotNull(message = "Ticket price is required")
        @Positive(message = "Ticket price must be positive")
        BigDecimal ticketPrice,

        @NotNull(message = "Ticket amount is required")
        @Min(value = 2, message = "Ticket amount must be at least 2")
        Integer ticketAmount,

        @Future(message = "Draw must happen on the future")
        LocalDateTime drawDate,

        @Valid
        @NotEmpty
        List<PrizeCreateDTO> prizes
) {
}
