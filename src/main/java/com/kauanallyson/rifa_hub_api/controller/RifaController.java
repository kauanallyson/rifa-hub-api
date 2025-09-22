package com.kauanallyson.rifa_hub_api.controller;

import com.kauanallyson.rifa_hub_api.dto.rifa.RifaCreateDTO;
import com.kauanallyson.rifa_hub_api.dto.rifa.RifaResponseDTO;
import com.kauanallyson.rifa_hub_api.services.RifaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
