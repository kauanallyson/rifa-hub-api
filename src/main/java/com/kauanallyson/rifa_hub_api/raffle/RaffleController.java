package com.kauanallyson.rifa_hub_api.raffle;

import com.kauanallyson.rifa_hub_api.raffle.dtos.RaffleCreate;
import com.kauanallyson.rifa_hub_api.raffle.dtos.RaffleResponse;
import com.kauanallyson.rifa_hub_api.raffle.dtos.RaffleUpdate;
import com.kauanallyson.rifa_hub_api.shared.enums.RaffleStatus;
import com.kauanallyson.rifa_hub_api.shared.enums.TicketStatus;
import com.kauanallyson.rifa_hub_api.ticket.TicketService;
import com.kauanallyson.rifa_hub_api.ticket.dtos.TicketOrderRequest;
import com.kauanallyson.rifa_hub_api.ticket.dtos.TicketResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/raffles")
@RequiredArgsConstructor
public class RaffleController {

    private final RaffleService raffleService;
    private final TicketService ticketService;

    // POST /api/raffles
    @PostMapping
    public ResponseEntity<RaffleResponse> createRaffle(@Valid @RequestBody RaffleCreate dto) {
        RaffleResponse response = raffleService.createRaffle(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // GET /api/raffles?name=...
    @GetMapping
    public ResponseEntity<List<RaffleResponse>> findAll(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) RaffleStatus status
    ) {
        return ResponseEntity.ok(raffleService.findAll(name, status));
    }

    // GET /api/raffles/{id}
    @GetMapping("/{id}")
    public ResponseEntity<RaffleResponse> findRaffleById(@PathVariable Long id) {
        return ResponseEntity.ok(raffleService.findRaffleById(id));
    }

    // PUT /api/raffles/{id}
    @PutMapping("/{id}")
    public ResponseEntity<RaffleResponse> updateRaffle(
            @PathVariable Long id,
            @Valid @RequestBody RaffleUpdate dto) {
        return ResponseEntity.ok(raffleService.updateRaffle(dto, id));
    }

    // DELETE /api/raffles/{id}
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRaffle(@PathVariable Long id) {
        raffleService.deleteRaffle(id);
    }

    // POST /api/raffles/{id}/draw
    @PostMapping("/{id}/draw")
    public ResponseEntity<RaffleResponse> drawWinningTickets(@PathVariable Long id) {
        return ResponseEntity.ok(raffleService.drawWinningTickets(id));
    }

    // GET /api/raffles/{id}/tickets?status=AVAILABLE
    @GetMapping("/{id}/tickets")
    public ResponseEntity<List<TicketResponse>> getTicketsByRaffle(
            @PathVariable Long id,
            @RequestParam(required = false) TicketStatus status) {
        return ResponseEntity.ok(ticketService.getTicketsByRaffleId(id, status));
    }

    // POST /api/raffles/{id}/tickets/sell
    @PostMapping("/{id}/tickets/sell")
    public ResponseEntity<List<TicketResponse>> sellTickets(
            @PathVariable Long id,
            @RequestBody @Valid TicketOrderRequest dto) {
        List<TicketResponse> response = ticketService.sellTickets(id, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}