package com.kauanallyson.rifa_hub_api.ticket;

import com.kauanallyson.rifa_hub_api.ticket.dtos.TicketResponse;
import org.springframework.stereotype.Component;

@Component
public class TicketMapper {

    public TicketResponse toResponseDTO(Ticket ticket) {
        if (ticket == null) {
            return null;
        }
        String nomeComprador = ticket.getClient() != null ? ticket.getClient().getName() : null;
        String nomeVendedor = ticket.getSeller() != null ? ticket.getSeller().getName() : null;

        return new TicketResponse(
                ticket.getNumber(),
                ticket.getStatus(),
                nomeComprador,
                nomeVendedor
        );
    }
}