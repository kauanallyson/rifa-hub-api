package com.kauanallyson.rifa_hub_api.utils.mappers;

import com.kauanallyson.rifa_hub_api.dtos.prize.PrizeCreate;
import com.kauanallyson.rifa_hub_api.dtos.prize.PrizeResponse;
import com.kauanallyson.rifa_hub_api.entities.Prize;
import com.kauanallyson.rifa_hub_api.entities.Raffle;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PrizeMapper {

    private final TicketMapper ticketMapper;

    public Prize toEntity(PrizeCreate dto, Raffle raffle) {
        Prize prize = new Prize();
        prize.setDescription(dto.description());
        prize.setPlacement(dto.placement());
        prize.setRaffle(raffle);
        return prize;
    }

    public PrizeResponse toResponseDTO(Prize prize) {
        return new PrizeResponse(
                prize.getDescription(),
                prize.getPlacement(),
                ticketMapper.toResponseDTO(prize.getWinningTicket())
        );
    }


}