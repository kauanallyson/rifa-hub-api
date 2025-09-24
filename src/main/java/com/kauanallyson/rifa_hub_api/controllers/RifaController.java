package com.kauanallyson.rifa_hub_api.controllers;

import com.kauanallyson.rifa_hub_api.dtos.rifa.RifaCreateDTO;
import com.kauanallyson.rifa_hub_api.dtos.rifa.RifaResponseDTO;
import com.kauanallyson.rifa_hub_api.dtos.rifa.RifaUpdateDTO;
import com.kauanallyson.rifa_hub_api.entities.enums.StatusRifa;
import com.kauanallyson.rifa_hub_api.services.RifaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rifas")
@RequiredArgsConstructor
public class RifaController {

    private final RifaService rifaService;

    // POST /api/rifas
    @PostMapping
    public ResponseEntity<RifaResponseDTO> createRifa(@Valid @RequestBody RifaCreateDTO dto) {
        RifaResponseDTO response = rifaService.createRifa(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // GET /api/rifas
    @GetMapping
    public ResponseEntity<Page<RifaResponseDTO>> getAllRifas(Pageable pageable) {
        return ResponseEntity.ok(rifaService.getAllRifas(pageable));
    }

    // GET /api/rifas/buscar-por-status?status=ABERTA
    @GetMapping("/buscar-por-status")
    public ResponseEntity<Page<RifaResponseDTO>> getRifasPorStatus(@RequestParam StatusRifa status, Pageable pageable) {
        return ResponseEntity.ok(rifaService.getAllRifas(pageable, status));
    }

    // GET /api/rifas/buscar-por-nome?nome=
    @GetMapping("/buscar-por-nome")
    public ResponseEntity<Page<RifaResponseDTO>> findRifasPorNome(@RequestParam String nome, Pageable pageable) {
        return ResponseEntity.ok(rifaService.findAllRifasByNome(pageable, nome));
    }

    // GET /api/rifas/{id}
    @GetMapping("/{id}")
    public ResponseEntity<RifaResponseDTO> findRifaById(@PathVariable Long id) {
        return ResponseEntity.ok(rifaService.findRifaById(id));
    }

    // PUT /api/rifas/{id}
    @PutMapping("/{id}")
    public ResponseEntity<RifaResponseDTO> updateRifa(@PathVariable Long id, @Valid @RequestBody RifaUpdateDTO dto) {
        return ResponseEntity.ok(rifaService.updateRifa(dto, id));
    }

    // DELETE /api/rifas/{id}
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRifa(@PathVariable Long id) {
        rifaService.deleteRifa(id);
    }

    // POST /api/rifas/{id}/realizar-sorteio
    @PostMapping("/{id}/realizar-sorteio")
    public ResponseEntity<RifaResponseDTO> realizarSorteio(@PathVariable Long id) {
        return ResponseEntity.ok(rifaService.realizarSorteio(id));
    }
}