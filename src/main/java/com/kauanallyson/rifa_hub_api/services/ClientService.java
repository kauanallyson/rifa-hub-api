package com.kauanallyson.rifa_hub_api.services;

import com.kauanallyson.rifa_hub_api.dtos.client.ClientCreate;
import com.kauanallyson.rifa_hub_api.dtos.client.ClientResponse;
import com.kauanallyson.rifa_hub_api.dtos.client.ClientUpdate;
import com.kauanallyson.rifa_hub_api.entities.Client;
import com.kauanallyson.rifa_hub_api.exceptions.BusinessException;
import com.kauanallyson.rifa_hub_api.exceptions.DuplicateResourceException;
import com.kauanallyson.rifa_hub_api.exceptions.ResourceNotFoundException;
import com.kauanallyson.rifa_hub_api.repositories.ClientRepository;
import com.kauanallyson.rifa_hub_api.utils.mappers.ClientMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    // Create
    @Transactional
    public ClientResponse createClient(ClientCreate dto) {
        if (dto.email() != null && !dto.email().isEmpty() && clientRepository.existsByEmail(dto.email())) {
            throw new DuplicateResourceException("Email not available");
        }

        if (clientRepository.existsByPhone(dto.phone())) {
            throw new DuplicateResourceException("Phone not available");
        }

        Client client = clientMapper.toEntity(dto);
        Client savedClient = clientRepository.save(client);
        return clientMapper.toResponseDTO(savedClient);
    }

    // Find all
    @Transactional(readOnly = true)
    public List<ClientResponse> findAll(String name) {
        String filterName = (name != null && !name.isBlank()) ? name : null;

        List<Client> clients = clientRepository.findAllActiveByName(filterName);

        return clients.stream()
                .map(clientMapper::toResponseDTO)
                .toList();
    }

    // Find Active By id
    @Transactional(readOnly = true)
    public ClientResponse findClientById(Long id) {
        Client client = clientRepository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client with id " + id + " not found"));
        return clientMapper.toResponseDTO(client);
    }

    // Update
    @Transactional
    public ClientResponse updateClient(Long id, ClientUpdate dto) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client with id " + id + " not found"));

        if (!client.isActive()) {
            throw new BusinessException("Client inactive");
        }

        validateEmailUpdate(dto.email(), id);
        validatePhoneUpdate(dto.phone(), id);

        clientMapper.updateEntityFromDTO(dto, client);
        clientRepository.save(client);
        return clientMapper.toResponseDTO(client);
    }

    // Delete
    @Transactional
    public void deleteClient(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client with id " + id + " not found"));

        if (!client.isActive()) {
            throw new BusinessException("Client already inactive");
        }

        client.setActive(false);
        clientRepository.save(client);
    }

    // ---------------
    private void validateEmailUpdate(String email, Long currentId) {
        if (email != null && !email.isBlank()) {
            Optional<Client> conflict = clientRepository.findByEmail(email);
            if (conflict.isPresent() && !conflict.get().getId().equals(currentId)) {
                throw new DuplicateResourceException("Email not available");
            }
        }
    }

    private void validatePhoneUpdate(String phone, Long currentId) {
        if (phone != null && !phone.isBlank()) {
            Optional<Client> conflict = clientRepository.findByPhone(phone);
            if (conflict.isPresent() && !conflict.get().getId().equals(currentId)) {
                throw new DuplicateResourceException("Phone not available");
            }
        }
    }
}