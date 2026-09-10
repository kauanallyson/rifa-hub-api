package com.kauanallyson.rifa_hub_api.client.dtos;

import com.kauanallyson.rifa_hub_api.ticket.dtos.TicketResponse;

import java.util.List;

public record ClientResponse(
        Long id,
        String name,
        String phone,
        String email,
        List<TicketResponse> ticketsPurchased
) {
}
