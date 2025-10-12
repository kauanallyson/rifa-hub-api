package com.kauanallyson.rifa_hub_api.controllers;

import com.kauanallyson.rifa_hub_api.dtos.client.ClientCreateDTO;
import com.kauanallyson.rifa_hub_api.dtos.client.ClientResponseDTO;
import com.kauanallyson.rifa_hub_api.dtos.client.ClientUpdateDTO;
import com.kauanallyson.rifa_hub_api.services.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    // POST /api/clients
    @PostMapping
    public ResponseEntity<ClientResponseDTO> createClient(@Valid @RequestBody ClientCreateDTO dto) {
        ClientResponseDTO response = clientService.createClient(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // GET /api/clients
    @GetMapping
    public ResponseEntity<List<ClientResponseDTO>> getAllActiveClients() {
        return ResponseEntity.ok(clientService.getAllActiveClients());
    }

    // GET /api/clients/search?name=
    @GetMapping("/search")
    public ResponseEntity<List<ClientResponseDTO>> findClientByName(
            @RequestParam String nome) {
        return ResponseEntity.ok(clientService.findClientByName(nome));
    }

    // GET /api/clients/{id}
    @GetMapping("/{id}")
    public ResponseEntity<ClientResponseDTO> findClientById(
            @PathVariable Long id) {
        return ResponseEntity.ok(clientService.findClientById(id));
    }

    // PUT /api/clients/{id}
    @PutMapping("/{id}")
    public ResponseEntity<ClientResponseDTO> updateClient(
            @PathVariable Long id,
            @Valid @RequestBody ClientUpdateDTO dto) {
        return ResponseEntity.ok(clientService.updateClient(id, dto));
    }

    // DELETE /api/clients/{id}
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteClient(
            @PathVariable Long id) {
        clientService.deleteClient(id);
    }
}