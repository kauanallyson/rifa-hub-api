package com.kauanallyson.rifa_hub_api.utils.mappers;

import com.kauanallyson.rifa_hub_api.dtos.raffle.RaffleCreateDTO;
import com.kauanallyson.rifa_hub_api.dtos.raffle.RaffleResponseDTO;
import com.kauanallyson.rifa_hub_api.dtos.raffle.RaffleUpdateDTO;
import com.kauanallyson.rifa_hub_api.entities.Prize;
import com.kauanallyson.rifa_hub_api.entities.Raffle;
import com.kauanallyson.rifa_hub_api.entities.enums.RaffleStatus;
import com.kauanallyson.rifa_hub_api.entities.enums.TicketStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RaffleMapper {

    private final PrizeMapper prizeMapper;

    public Raffle toEntity(RaffleCreateDTO dto) {
        Raffle raffle = new Raffle();
        raffle.setName(dto.name());
        raffle.setDescription(dto.description());
        raffle.setTicketPrice(dto.ticketPrice());
        raffle.setTicketAmount(dto.ticketAmount());
        raffle.setDrawDate(dto.drawDate());
        raffle.setStatus(RaffleStatus.OPEN);

        List<Prize> prizes = dto.prizes().stream()
                .map(premioDto -> prizeMapper.toEntity(premioDto, raffle))
                .collect(Collectors.toList());
        raffle.setPrizes(prizes);

        return raffle;
    }

    public RaffleResponseDTO toResponseDTO(Raffle raffle) {
        long pontosVendidos = raffle.getTickets().stream()
                .filter(p -> p.getStatus() == TicketStatus.SOLD)
                .count();

        return new RaffleResponseDTO(
                raffle.getId(),
                raffle.getName(),
                raffle.getDescription(),
                raffle.getPrizes().stream().map(prizeMapper::toResponseDTO).collect(Collectors.toList()),
                raffle.getTicketPrice(),
                raffle.getTicketAmount(),
                pontosVendidos,
                raffle.getTicketAmount() - pontosVendidos,
                raffle.getDrawDate(),
                raffle.getStatus()
        );
    }

    public void updateEntityFromDTO(RaffleUpdateDTO dto, Raffle raffle) {
        raffle.setName(dto.name());
        raffle.setDescription(dto.description());
        raffle.setTicketPrice(dto.ticketPrice());
        raffle.setDrawDate(dto.drawDate());
    }
}