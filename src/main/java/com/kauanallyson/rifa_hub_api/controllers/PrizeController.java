package com.kauanallyson.rifa_hub_api.controllers;

import com.kauanallyson.rifa_hub_api.dtos.prize.PrizeCreateDTO;
import com.kauanallyson.rifa_hub_api.dtos.prize.PrizeResponseDTO;
import com.kauanallyson.rifa_hub_api.services.PrizeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/raffles/{id}/prizes")
@RequiredArgsConstructor
public class PrizeController {

    private final PrizeService prizeService;

    // POST /api/raffles/{id}/prizes
    @PostMapping
    public ResponseEntity<PrizeResponseDTO> addPrize(
            @PathVariable Long raffleId,
            @Valid @RequestBody PrizeCreateDTO dto) {
        PrizeResponseDTO response = prizeService.addPrize(raffleId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // DELETE /api/raffles/{raffleId}/prizes/{prizeId}
    @DeleteMapping("/{prizeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePrize(
            @PathVariable Long raffleId,
            @PathVariable Long prizeId) {
        prizeService.deletePrize(raffleId, prizeId);
    }
}