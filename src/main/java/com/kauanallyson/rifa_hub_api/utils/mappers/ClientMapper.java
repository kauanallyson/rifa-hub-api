package com.kauanallyson.rifa_hub_api.utils.mappers;

import com.kauanallyson.rifa_hub_api.dtos.client.ClientCreate;
import com.kauanallyson.rifa_hub_api.dtos.client.ClientResponse;
import com.kauanallyson.rifa_hub_api.dtos.client.ClientUpdate;
import com.kauanallyson.rifa_hub_api.dtos.ticket.TicketResponse;
import com.kauanallyson.rifa_hub_api.entities.Client;
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