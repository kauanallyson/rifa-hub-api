package com.kauanallyson.rifa_hub_api.utils.mappers;

import com.kauanallyson.rifa_hub_api.dtos.seller.SellerCreate;
import com.kauanallyson.rifa_hub_api.dtos.seller.SellerResponse;
import com.kauanallyson.rifa_hub_api.dtos.seller.SellerUpdate;
import com.kauanallyson.rifa_hub_api.dtos.ticket.TicketResponse;
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

    public Seller toEntity(SellerCreate dto) {
        Seller seller = new Seller();
        seller.setName(dto.name());
        seller.setPhone(dto.phone());
        seller.setEmail(dto.email());
        return seller;
    }

    public SellerResponse toResponseDTO(Seller seller) {
        List<TicketResponse> pontosDto = seller.getTicketsSold() != null ?
                seller.getTicketsSold().stream()
                        .map(ticketMapper::toResponseDTO)
                        .collect(Collectors.toList()) :
                Collections.emptyList();

        return new SellerResponse(
                seller.getId(),
                seller.getName(),
                seller.getPhone(),
                seller.getEmail(),
                pontosDto
        );
    }

    public void updateEntityFromDTO(SellerUpdate dto, Seller seller) {
        seller.setName(dto.name());
        seller.setPhone(dto.phone());
        seller.setEmail(dto.email());
    }
}