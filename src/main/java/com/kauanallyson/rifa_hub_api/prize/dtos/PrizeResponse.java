package com.kauanallyson.rifa_hub_api.prize.dtos;

import com.kauanallyson.rifa_hub_api.ticket.dtos.TicketResponse;

public record PrizeResponse(
        String description,
        Integer placement,
        TicketResponse winningTicket
) {
}
