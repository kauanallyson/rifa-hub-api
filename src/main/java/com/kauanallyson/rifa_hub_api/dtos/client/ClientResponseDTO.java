package com.kauanallyson.rifa_hub_api.dtos.client;

import com.kauanallyson.rifa_hub_api.dtos.ticket.TicketResponseDTO;

import java.util.List;

public record ClientResponseDTO(
        Long id,
        String name,
        String phone,
        String email,
        List<TicketResponseDTO> ticketsPurchased
) {
}
