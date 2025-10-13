package com.kauanallyson.rifa_hub_api.controllers;

import com.kauanallyson.rifa_hub_api.dtos.raffle.RaffleCreateDTO;
import com.kauanallyson.rifa_hub_api.dtos.raffle.RaffleResponseDTO;
import com.kauanallyson.rifa_hub_api.dtos.raffle.RaffleUpdateDTO;
import com.kauanallyson.rifa_hub_api.entities.enums.RaffleStatus;
import com.kauanallyson.rifa_hub_api.services.RaffleService;
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

    // POST /api/raffles
    @PostMapping
    public ResponseEntity<RaffleResponseDTO> createRaffle(
            @Valid @RequestBody RaffleCreateDTO dto) {
        RaffleResponseDTO response = raffleService.createRaffle(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // GET /api/raffles
    @GetMapping
    public ResponseEntity<List<RaffleResponseDTO>> getAllActiveRaffles() {
        return ResponseEntity.ok(raffleService.getAllActiveRaffles());
    }

    // GET /api/raffles/{raffleId}
    @GetMapping("/{id}")
    public ResponseEntity<RaffleResponseDTO> findRaffleById(
            @PathVariable Long id) {
        return ResponseEntity.ok(raffleService.findRaffleById(id));
    }

    // GET /api/rifas/by-status?status=
    @GetMapping("/by-status")
    public ResponseEntity<List<RaffleResponseDTO>> getRafflesByStatus(
            @RequestParam RaffleStatus status) {
        return ResponseEntity.ok(raffleService.getAllRaffleByStatus(status));
    }

    // GET /api/rifas/by-name?name=
    @GetMapping("/by-name")
    public ResponseEntity<List<RaffleResponseDTO>> findRaffleByName(
            @RequestParam String name) {
        return ResponseEntity.ok(raffleService.findRaffleByName(name));
    }

    // PUT /api/raffles/{raffleId}
    @PutMapping("/{id}")
    public ResponseEntity<RaffleResponseDTO> updateRaffle(
            @PathVariable Long id,
            @Valid @RequestBody RaffleUpdateDTO dto) {
        return ResponseEntity.ok(raffleService.updateRaffle(dto, id));
    }

    // DELETE /api/raffles/{raffleId}
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRaffle(
            @PathVariable Long id) {
        raffleService.deleteRaffle(id);
    }

    // POST /api/raffles/{raffleId}/draw
    @PostMapping("/{id}/draw")
    public ResponseEntity<RaffleResponseDTO> drawWinningTickets(@PathVariable Long id) {
        return ResponseEntity.ok(raffleService.drawWinningTickets(id));
    }
}