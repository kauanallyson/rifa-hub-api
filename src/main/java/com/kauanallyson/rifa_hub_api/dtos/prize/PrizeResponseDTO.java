package com.kauanallyson.rifa_hub_api.dtos.prize;

import com.kauanallyson.rifa_hub_api.dtos.ticket.TicketResponseDTO;

public record PrizeResponseDTO(
        String description,
        Integer placing,
        TicketResponseDTO winningTicket
) {
}
