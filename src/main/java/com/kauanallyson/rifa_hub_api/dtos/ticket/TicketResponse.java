package com.kauanallyson.rifa_hub_api.dtos.ticket;

import com.kauanallyson.rifa_hub_api.enums.TicketStatus;

public record TicketResponse(
        Long number,
        TicketStatus status,
        String clientName,
        String sellerName
) {
}
