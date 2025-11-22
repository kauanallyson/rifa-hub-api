package com.kauanallyson.rifa_hub_api.dtos.seller;

import com.kauanallyson.rifa_hub_api.dtos.ticket.TicketResponse;

import java.util.List;

public record SellerResponse(
        Long id,
        String name,
        String phone,
        String email,
        List<TicketResponse> ticketsSold
) {
}
