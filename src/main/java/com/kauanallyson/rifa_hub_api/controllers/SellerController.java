package com.kauanallyson.rifa_hub_api.controllers;

import com.kauanallyson.rifa_hub_api.dtos.seller.SellerCreate;
import com.kauanallyson.rifa_hub_api.dtos.seller.SellerResponse;
import com.kauanallyson.rifa_hub_api.dtos.seller.SellerUpdate;
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
    public ResponseEntity<SellerResponse> createSeller(@Valid @RequestBody SellerCreate dto) {
        SellerResponse response = sellerService.createSeller(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // GET /api/sellers
    @GetMapping
    public ResponseEntity<List<SellerResponse>> findAll(@RequestParam(required = false) String name) {
        return ResponseEntity.ok(sellerService.findAll(name));
    }

    // GET /api/sellers/{id}
    @GetMapping("/{id}")
    public ResponseEntity<SellerResponse> findSellerById(@PathVariable Long id) {
        return ResponseEntity.ok(sellerService.findSellerById(id));
    }

    // PUT /api/sellers/{id}
    @PutMapping("/{id}")
    public ResponseEntity<SellerResponse> updateSeller(@PathVariable Long id, @Valid @RequestBody SellerUpdate dto) {
        return ResponseEntity.ok(sellerService.updateSeller(id, dto));
    }

    // DELETE /api/sellers/{id}
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSeller(@PathVariable Long id) {
        sellerService.deleteSeller(id);
    }
}