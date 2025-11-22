package com.kauanallyson.rifa_hub_api.dtos.prize;

import com.kauanallyson.rifa_hub_api.dtos.ticket.TicketResponse;

public record PrizeResponse(
        String description,
        Integer placement,
        TicketResponse winningTicket
) {
}
