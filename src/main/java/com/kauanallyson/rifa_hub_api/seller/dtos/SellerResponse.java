package com.kauanallyson.rifa_hub_api.seller.dtos;

import com.kauanallyson.rifa_hub_api.ticket.dtos.TicketResponse;

import java.util.List;

public record SellerResponse(
        Long id,
        String name,
        String phone,
        String email,
        List<TicketResponse> ticketsSold
) {
}
