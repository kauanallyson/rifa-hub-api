package com.kauanallyson.rifa_hub_api.utils.mappers;

import com.kauanallyson.rifa_hub_api.dtos.client.ClientCreateDTO;
import com.kauanallyson.rifa_hub_api.dtos.client.ClientResponseDTO;
import com.kauanallyson.rifa_hub_api.dtos.client.ClientUpdateDTO;
import com.kauanallyson.rifa_hub_api.dtos.ticket.TicketResponseDTO;
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

    public Client toEntity(ClientCreateDTO dto) {
        Client client = new Client();
        client.setName(dto.name());
        client.setPhone(dto.phone());
        client.setEmail(dto.email());
        return client;
    }

    public ClientResponseDTO toResponseDTO(Client client) {
        List<TicketResponseDTO> pontosDto = client.getTicketsPurchased() != null ?
                client.getTicketsPurchased().stream()
                        .map(ticketMapper::toResponseDTO)
                        .collect(Collectors.toList())
                : Collections.emptyList();

        return new ClientResponseDTO(
                client.getId(),
                client.getName(),
                client.getPhone(),
                client.getEmail(),
                pontosDto
        );
    }

    public void updateEntityFromDTO(ClientUpdateDTO dto, Client client) {
        client.setName(dto.name());
        client.setPhone(dto.phone());
        client.setEmail(dto.email());
    }
}