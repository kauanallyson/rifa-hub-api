package com.kauanallyson.rifa_hub_api.dtos.seller;

import com.kauanallyson.rifa_hub_api.dtos.ticket.TicketResponseDTO;

import java.util.List;

public record SellerResponseDTO(
        Long id,
        String name,
        String phone,
        String email,
        List<TicketResponseDTO> ticketsSold
) {
}
