package com.kauanallyson.rifa_hub_api.controllers;

import com.kauanallyson.rifa_hub_api.dtos.client.ClientCreate;
import com.kauanallyson.rifa_hub_api.dtos.client.ClientResponse;
import com.kauanallyson.rifa_hub_api.dtos.client.ClientUpdate;
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
    public ResponseEntity<ClientResponse> createClient(@Valid @RequestBody ClientCreate dto) {
        ClientResponse response = clientService.createClient(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // GET /api/clients
    @GetMapping
    public ResponseEntity<List<ClientResponse>> findAll(@RequestParam(required = false) String name) {
        return ResponseEntity.ok(clientService.findAll(name));
    }

    // GET /api/clients/{id}
    @GetMapping("/{id}")
    public ResponseEntity<ClientResponse> findClientById(@PathVariable Long id) {
        return ResponseEntity.ok(clientService.findClientById(id));
    }

    // PUT /api/clients/{id}
    @PutMapping("/{id}")
    public ResponseEntity<ClientResponse> updateClient(@PathVariable Long id, @Valid @RequestBody ClientUpdate dto) {
        return ResponseEntity.ok(clientService.updateClient(id, dto));
    }

    // DELETE /api/clients/{id}
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);
    }
}