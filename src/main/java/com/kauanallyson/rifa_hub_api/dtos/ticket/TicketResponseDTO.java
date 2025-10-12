package com.kauanallyson.rifa_hub_api.dtos.ticket;

import com.kauanallyson.rifa_hub_api.entities.enums.TicketStatus;

public record TicketResponseDTO(
        Integer number,
        TicketStatus status,
        String clientName,
        String sellerName
) {
}
