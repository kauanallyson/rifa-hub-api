package com.kauanallyson.rifa_hub_api.utils.mappers;

import com.kauanallyson.rifa_hub_api.dtos.ticket.TicketResponseDTO;
import com.kauanallyson.rifa_hub_api.entities.Ticket;
import org.springframework.stereotype.Component;

@Component
public class TicketMapper {

    public TicketResponseDTO toResponseDTO(Ticket ticket) {
        if (ticket == null) {
            return null;
        }
        String nomeComprador = ticket.getClient() != null ? ticket.getClient().getName() : null;
        String nomeVendedor = ticket.getSeller() != null ? ticket.getSeller().getName() : null;

        return new TicketResponseDTO(
                ticket.getNumber(),
                ticket.getStatus(),
                nomeComprador,
                nomeVendedor
        );
    }
}