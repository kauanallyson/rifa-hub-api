package com.kauanallyson.rifa_hub_api.prize;

import com.kauanallyson.rifa_hub_api.prize.dtos.PrizeCreate;
import com.kauanallyson.rifa_hub_api.prize.dtos.PrizeResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/raffles/{raffleId}/prizes")
@RequiredArgsConstructor
public class PrizeController {

    private final PrizeService prizeService;

    // POST /api/raffles/{id}/prizes
    @PostMapping
    public ResponseEntity<PrizeResponse> addPrize(
            @PathVariable Long raffleId,
            @Valid @RequestBody PrizeCreate dto) {
        PrizeResponse response = prizeService.addPrize(raffleId, dto);
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