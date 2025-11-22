package com.kauanallyson.rifa_hub_api.dtos.client;

import com.kauanallyson.rifa_hub_api.dtos.ticket.TicketResponse;

import java.util.List;

public record ClientResponse(
        Long id,
        String name,
        String phone,
        String email,
        List<TicketResponse> ticketsPurchased
) {
}
