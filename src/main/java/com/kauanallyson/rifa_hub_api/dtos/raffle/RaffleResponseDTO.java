package com.kauanallyson.rifa_hub_api.dtos.raffle;

import com.kauanallyson.rifa_hub_api.dtos.prize.PrizeResponseDTO;
import com.kauanallyson.rifa_hub_api.entities.enums.RaffleStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record RaffleResponseDTO(
        Long id,
        String name,
        String description,
        List<PrizeResponseDTO> prizes,
        BigDecimal ticketPrice,
        Integer ticketAmount,
        Long ticketsSold,
        Long ticketsAvailable,
        LocalDateTime drawDate,
        RaffleStatus status
) {
}
