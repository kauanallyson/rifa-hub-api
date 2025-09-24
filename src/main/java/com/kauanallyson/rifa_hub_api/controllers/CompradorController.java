package com.kauanallyson.rifa_hub_api.controllers;

import com.kauanallyson.rifa_hub_api.dtos.comprador.CompradorCreateDTO;
import com.kauanallyson.rifa_hub_api.dtos.comprador.CompradorResponseDTO;
import com.kauanallyson.rifa_hub_api.dtos.comprador.CompradorUpdateDTO;
import com.kauanallyson.rifa_hub_api.services.CompradorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/compradores")
@RequiredArgsConstructor
public class CompradorController {

    private final CompradorService compradorService;

    // POST /api/compradores
    @PostMapping
    public ResponseEntity<CompradorResponseDTO> createComprador(@Valid @RequestBody CompradorCreateDTO dto) {
        CompradorResponseDTO response = compradorService.createComprador(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // GET /api/compradores
    @GetMapping
    public ResponseEntity<Page<CompradorResponseDTO>> getAllCompradores(Pageable pageable) {
        return ResponseEntity.ok(compradorService.getAllCompradores(pageable));
    }

    // GET /api/compradores/buscar-por-nome?nome=
    @GetMapping("/buscar-por-nome")
    public ResponseEntity<Page<CompradorResponseDTO>> findCompradorPorNome(@RequestParam String nome, Pageable pageable) {
        return ResponseEntity.ok(compradorService.findCompradorByNome(nome, pageable));
    }

    // GET /api/compradores/{id}
    @GetMapping("/{id}")
    public ResponseEntity<CompradorResponseDTO> findCompradorById(@PathVariable Long id) {
        return ResponseEntity.ok(compradorService.findCompradorById(id));
    }

    // PUT /api/compradores/{id}
    @PutMapping("/{id}")
    public ResponseEntity<CompradorResponseDTO> updateComprador(@PathVariable Long id, @Valid @RequestBody CompradorUpdateDTO dto) {
        return ResponseEntity.ok(compradorService.updateComprador(id, dto));
    }

    // DELETE /api/compradores/{id}
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComprador(@PathVariable Long id) {
        compradorService.deleteComprador(id);
    }
}