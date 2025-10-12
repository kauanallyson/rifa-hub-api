package com.kauanallyson.rifa_hub_api.controllers;

import com.kauanallyson.rifa_hub_api.dtos.ticket.TicketOrderRequestDTO;
import com.kauanallyson.rifa_hub_api.dtos.ticket.TicketResponseDTO;
import com.kauanallyson.rifa_hub_api.services.TicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/raffles/{id}/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    // GET /api/raffles/{raffleId}/tickets
    @GetMapping
    public ResponseEntity<List<TicketResponseDTO>> getTicketsByRaffleId(
            @PathVariable Long raffleId) {
        return ResponseEntity.ok(ticketService.getTicketsByRaffleId(raffleId));
    }

    // POST /api/raffles/{raffleId}/tickets/sell
    @PostMapping("/sell")
    public ResponseEntity<List<TicketResponseDTO>> sellTickets(
            @PathVariable Long rifaId,
            @Valid @RequestBody TicketOrderRequestDTO dto) {
        return ResponseEntity.ok(ticketService.sellTickets(rifaId, dto));
    }
}