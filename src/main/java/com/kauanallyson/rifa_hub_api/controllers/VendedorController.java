package com.kauanallyson.rifa_hub_api.controllers;

import com.kauanallyson.rifa_hub_api.dtos.vendedor.VendedorCreateDTO;
import com.kauanallyson.rifa_hub_api.dtos.vendedor.VendedorResponseDTO;
import com.kauanallyson.rifa_hub_api.dtos.vendedor.VendedorUpdateDTO;
import com.kauanallyson.rifa_hub_api.services.VendedorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vendedores")
@RequiredArgsConstructor
public class VendedorController {

    private final VendedorService vendedorService;

    // POST /api/vendedores
    @PostMapping
    public ResponseEntity<VendedorResponseDTO> createVendedor(@Valid @RequestBody VendedorCreateDTO dto) {
        VendedorResponseDTO response = vendedorService.createVendedor(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // GET /api/vendedores
    @GetMapping
    public ResponseEntity<Page<VendedorResponseDTO>> getAllVendedores(Pageable pageable) {
        return ResponseEntity.ok(vendedorService.getAllVendedores(pageable));
    }

    // GET /api/vendedores/buscar-por-nome?nome=
    @GetMapping("/buscar-por-nome")
    public ResponseEntity<Page<VendedorResponseDTO>> findVendedorPorNome(@RequestParam String nome, Pageable pageable) {
        return ResponseEntity.ok(vendedorService.findVendedorByNome(nome, pageable));
    }

    // GET /api/vendedores/{id}
    @GetMapping("/{id}")
    public ResponseEntity<VendedorResponseDTO> findVendedorById(@PathVariable Long id) {
        return ResponseEntity.ok(vendedorService.findVendedorById(id));
    }

    // PUT /api/vendedores/{id}
    @PutMapping("/{id}")
    public ResponseEntity<VendedorResponseDTO> updateVendedor(@PathVariable Long id, @Valid @RequestBody VendedorUpdateDTO dto) {
        return ResponseEntity.ok(vendedorService.updateVendedor(dto, id));
    }

    // DELETE /api/vendedores/{id}
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteVendedor(@PathVariable Long id) {
        vendedorService.deleteVendedor(id);
    }
}