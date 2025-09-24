package com.kauanallyson.rifa_hub_api.controllers;

import com.kauanallyson.rifa_hub_api.dtos.ponto.PontoResponseDTO;
import com.kauanallyson.rifa_hub_api.dtos.ponto.PontoVendaRequestDTO;
import com.kauanallyson.rifa_hub_api.services.PontoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rifas/{rifaId}/pontos")
@RequiredArgsConstructor
public class PontoController {

    private final PontoService pontoService;

    // GET /api/rifas/{rifaId}/pontos
    @GetMapping
    public ResponseEntity<Page<PontoResponseDTO>> getPontosByRifa(
            @PathVariable Long rifaId,
            Pageable pageable) {
        return ResponseEntity.ok(pontoService.getPontosByRifa(rifaId, pageable));
    }

    // POST /api/rifas/{rifaId}/pontos/{numeroPonto}/vender
    @PostMapping("/{numeroPonto}/vender")
    public ResponseEntity<PontoResponseDTO> venderPonto(
            @PathVariable Long rifaId,
            @PathVariable Integer numeroPonto,
            @Valid @RequestBody PontoVendaRequestDTO dto) {
        return ResponseEntity.ok(pontoService.venderPonto(rifaId, numeroPonto, dto));
    }
}