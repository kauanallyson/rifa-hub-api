package com.kauanallyson.rifa_hub_api.controller;

import com.kauanallyson.rifa_hub_api.dto.rifa.RifaCreateDTO;
import com.kauanallyson.rifa_hub_api.dto.rifa.RifaResponseDTO;
import com.kauanallyson.rifa_hub_api.services.RifaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rifas")
public class RifaController {
    @Autowired
    RifaService rifaService;

    @PostMapping
    public ResponseEntity<RifaResponseDTO> createRifa(RifaCreateDTO dto){
        RifaResponseDTO novaRifa = rifaService.createRifa(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaRifa);
    }

    @GetMapping
    public ResponseEntity<Page<RifaResponseDTO>> getAllRifas(Pageable pageable){
        return ResponseEntity.ok(rifaService.getAllRifas(pageable));
    }

    @GetMapping("{id}")
    public ResponseEntity<RifaResponseDTO> findRifaById(@PathVariable Long id){
        RifaResponseDTO rifa = rifaService.findRifaById(id);
        return ResponseEntity.ok(rifa);
    }
}
