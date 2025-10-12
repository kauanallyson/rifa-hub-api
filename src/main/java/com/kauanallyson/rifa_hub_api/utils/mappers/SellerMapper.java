package com.kauanallyson.rifa_hub_api.utils.mappers;

import com.kauanallyson.rifa_hub_api.dtos.ticket.TicketResponseDTO;
import com.kauanallyson.rifa_hub_api.dtos.seller.SellerCreateDTO;
import com.kauanallyson.rifa_hub_api.dtos.seller.SellerResponseDTO;
import com.kauanallyson.rifa_hub_api.dtos.seller.SellerUpdateDTO;
import com.kauanallyson.rifa_hub_api.entities.Seller;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SellerMapper {

    private final TicketMapper ticketMapper;

    public Seller toEntity(SellerCreateDTO dto) {
        Seller seller = new Seller();
        seller.setName(dto.name());
        seller.setPhone(dto.phone());
        seller.setEmail(dto.email());
        return seller;
    }

    public SellerResponseDTO toResponseDTO(Seller seller) {
        List<TicketResponseDTO> pontosDto = seller.getTicketsSold() != null ?
                seller.getTicketsSold().stream()
                        .map(ticketMapper::toResponseDTO)
                        .collect(Collectors.toList()) :
                Collections.emptyList();

        return new SellerResponseDTO(
                seller.getId(),
                seller.getName(),
                seller.getPhone(),
                seller.getEmail(),
                pontosDto
        );
    }

    public void updateEntityFromDTO(SellerUpdateDTO dto, Seller seller) {
        seller.setName(dto.name());
        seller.setPhone(dto.phone());
        seller.setEmail(dto.email());
    }
}