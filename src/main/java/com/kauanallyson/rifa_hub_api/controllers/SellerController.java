package com.kauanallyson.rifa_hub_api.controllers;

import com.kauanallyson.rifa_hub_api.dtos.seller.SellerCreateDTO;
import com.kauanallyson.rifa_hub_api.dtos.seller.SellerResponseDTO;
import com.kauanallyson.rifa_hub_api.dtos.seller.SellerUpdateDTO;
import com.kauanallyson.rifa_hub_api.services.SellerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sellers")
@RequiredArgsConstructor
public class SellerController {

    private final SellerService sellerService;

    // POST /api/sellers
    @PostMapping
    public ResponseEntity<SellerResponseDTO> createSeller(
            @Valid @RequestBody SellerCreateDTO dto) {
        SellerResponseDTO response = sellerService.createSeller(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // GET /api/sellers
    @GetMapping
    public ResponseEntity<List<SellerResponseDTO>> getAllVendedores() {
        return ResponseEntity.ok(sellerService.getAllSellers());
    }

    // GET /api/sellers/search?name=
    @GetMapping("/search")
    public ResponseEntity<List<SellerResponseDTO>> findSellerByName(
            @RequestParam String nome) {
        return ResponseEntity.ok(sellerService.findSellerByName(nome));
    }

    // GET /api/sellers/{id}
    @GetMapping("/{id}")
    public ResponseEntity<SellerResponseDTO> findSellerById(@PathVariable Long id) {
        return ResponseEntity.ok(sellerService.findSellerById(id));
    }

    // PUT /api/sellers/{id}
    @PutMapping("/{id}")
    public ResponseEntity<SellerResponseDTO> UpdateSeller(
            @PathVariable Long id,
            @Valid @RequestBody SellerUpdateDTO dto) {
        return ResponseEntity.ok(sellerService.updateSeller(id, dto));
    }

    // DELETE /api/sellers/{id}
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSeller(
            @PathVariable Long id) {
        sellerService.deleteSeller(id);
    }
}