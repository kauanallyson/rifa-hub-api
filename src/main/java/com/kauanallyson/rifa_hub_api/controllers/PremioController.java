package com.kauanallyson.rifa_hub_api.controllers;

import com.kauanallyson.rifa_hub_api.dtos.premio.PremioCreateDTO;
import com.kauanallyson.rifa_hub_api.dtos.premio.PremioResponseDTO;
import com.kauanallyson.rifa_hub_api.services.PremioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rifas/{rifaId}/premios")
@RequiredArgsConstructor
public class PremioController {

    private final PremioService premioService;

    // POST /api/rifas/{rifaId}/premios
    @PostMapping
    public ResponseEntity<PremioResponseDTO> adicionarPremio(
            @PathVariable Long rifaId,
            @Valid @RequestBody PremioCreateDTO dto) {
        PremioResponseDTO response = premioService.adicionarPremio(rifaId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // DELETE /api/rifas/{rifaId}/premios/{premioId}
    @DeleteMapping("/{premioId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removerPremio(
            @PathVariable Long rifaId,
            @PathVariable Long premioId) {
        premioService.removerPremio(rifaId, premioId);
    }
}