package com.kauanallyson.rifa_hub_api.client;

import com.kauanallyson.rifa_hub_api.client.dtos.ClientCreate;
import com.kauanallyson.rifa_hub_api.client.dtos.ClientResponse;
import com.kauanallyson.rifa_hub_api.client.dtos.ClientUpdate;
import com.kauanallyson.rifa_hub_api.ticket.TicketMapper;
import com.kauanallyson.rifa_hub_api.ticket.dtos.TicketResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ClientMapper {

    private final TicketMapper ticketMapper;

    public Client toEntity(ClientCreate dto) {
        Client client = new Client();
        client.setName(dto.name());
        client.setPhone(dto.phone());
        client.setEmail(dto.email());
        return client;
    }

    public ClientResponse toResponseDTO(Client client) {
        List<TicketResponse> pontosDto = client.getTicketsPurchased() != null ?
                client.getTicketsPurchased().stream()
                        .map(ticketMapper::toResponseDTO)
                        .collect(Collectors.toList())
                : Collections.emptyList();

        return new ClientResponse(
                client.getId(),
                client.getName(),
                client.getPhone(),
                client.getEmail(),
                pontosDto
        );
    }

    public void updateEntityFromDTO(ClientUpdate dto, Client client) {
        client.setName(dto.name());
        client.setPhone(dto.phone());
        client.setEmail(dto.email());
    }
}