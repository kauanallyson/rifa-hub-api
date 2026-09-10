package com.kauanallyson.rifa_hub_api.ticket.dtos;

import com.kauanallyson.rifa_hub_api.shared.enums.TicketStatus;

public record TicketResponse(
        Long number,
        TicketStatus status,
        String clientName,
        String sellerName
) {
}
