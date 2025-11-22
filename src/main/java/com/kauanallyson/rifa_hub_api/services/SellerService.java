package com.kauanallyson.rifa_hub_api.services;

import com.kauanallyson.rifa_hub_api.dtos.seller.SellerCreate;
import com.kauanallyson.rifa_hub_api.dtos.seller.SellerResponse;
import com.kauanallyson.rifa_hub_api.dtos.seller.SellerUpdate;
import com.kauanallyson.rifa_hub_api.entities.Seller;
import com.kauanallyson.rifa_hub_api.exceptions.BusinessException;
import com.kauanallyson.rifa_hub_api.exceptions.DuplicateResourceException;
import com.kauanallyson.rifa_hub_api.exceptions.ResourceNotFoundException;
import com.kauanallyson.rifa_hub_api.repositories.SellerRepository;
import com.kauanallyson.rifa_hub_api.utils.mappers.SellerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SellerService {
    private final SellerRepository sellerRepository;
    private final SellerMapper sellerMapper;

    // Create
    @Transactional
    public SellerResponse createSeller(SellerCreate dto) {
        if (sellerRepository.existsByEmail(dto.email())) {
            throw new DuplicateResourceException("Email not available");
        }

        if (sellerRepository.existsByPhone(dto.phone())) {
            throw new DuplicateResourceException("Phone not available");
        }

        Seller seller = sellerMapper.toEntity(dto);
        Seller savedSeller = sellerRepository.save(seller);
        return sellerMapper.toResponseDTO(savedSeller);
    }

    // Find All (Unified)
    @Transactional(readOnly = true)
    public List<SellerResponse> findAll(String name) {
        String filterName = (name != null && !name.isBlank()) ? name : null;

        List<Seller> sellers = sellerRepository.findAllActiveByName(filterName);

        return sellers.stream()
                .map(sellerMapper::toResponseDTO)
                .toList();
    }

    // Find Active By id
    @Transactional(readOnly = true)
    public SellerResponse findSellerById(Long id) {
        Seller seller = sellerRepository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vendedor com id " + id + " não encontrado"));
        return sellerMapper.toResponseDTO(seller);
    }

    // Update
    @Transactional
    public SellerResponse updateSeller(Long id, SellerUpdate dto) {
        Seller seller = sellerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Seller with id " + id + " not found"));

        if (!seller.isActive()) {
            throw new BusinessException("Seller inactive");
        }

        validateEmailUpdate(dto.email(), id);
        validatePhoneUpdate(dto.phone(), id);

        sellerMapper.updateEntityFromDTO(dto, seller);
        sellerRepository.save(seller);
        return sellerMapper.toResponseDTO(seller);
    }

    // Delete
    @Transactional
    public void deleteSeller(Long id) {
        Seller seller = sellerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Seller with " + id + " not found"));

        if (!seller.isActive()) {
            throw new BusinessException("Seller already inactive");
        }

        seller.setActive(false);
        sellerRepository.save(seller);
    }

    // ---------------
    private void validateEmailUpdate(String email, Long currentId) {
        if (email != null && !email.isBlank()) {
            Optional<Seller> conflict = sellerRepository.findByEmail(email);
            if (conflict.isPresent() && !conflict.get().getId().equals(currentId)) {
                throw new DuplicateResourceException("Email not available");
            }
        }
    }

    private void validatePhoneUpdate(String phone, Long currentId) {
        if (phone != null && !phone.isBlank()) {
            Optional<Seller> conflict = sellerRepository.findByPhone(phone);
            if (conflict.isPresent() && !conflict.get().getId().equals(currentId)) {
                throw new DuplicateResourceException("Phone not available");
            }
        }
    }
}