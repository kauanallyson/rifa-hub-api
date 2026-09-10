package com.kauanallyson.rifa_hub_api.raffle.dtos;

import com.kauanallyson.rifa_hub_api.prize.dtos.PrizeResponse;
import com.kauanallyson.rifa_hub_api.shared.enums.RaffleStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record RaffleResponse(
        Long id,
        String name,
        String description,
        List<PrizeResponse> prizes,
        BigDecimal ticketPrice,
        Long ticketAmount,
        Long ticketsSold,
        Long ticketsAvailable,
        LocalDateTime drawDate,
        RaffleStatus status
) {
}
