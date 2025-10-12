package com.kauanallyson.rifa_hub_api.services;

import com.kauanallyson.rifa_hub_api.dtos.client.ClientCreateDTO;
import com.kauanallyson.rifa_hub_api.dtos.client.ClientResponseDTO;
import com.kauanallyson.rifa_hub_api.dtos.client.ClientUpdateDTO;
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
    public ClientResponseDTO createClient(ClientCreateDTO dto) {
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

    // Get all Active
    @Transactional(readOnly = true)
    public List<ClientResponseDTO> getAllActiveClients() {
        List<Client> activeClients = clientRepository.findAllActive();
        return activeClients.stream()
                .map(clientMapper::toResponseDTO).
                toList();
    }

    // Find Active By id
    @Transactional(readOnly = true)
    public ClientResponseDTO findClientById(Long id) {
        Client client = clientRepository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client with id " + id + " not found"));
        return clientMapper.toResponseDTO(client);
    }

    // Find Active By Name
    @Transactional(readOnly = true)
    public List<ClientResponseDTO> findClientByName(String nome) {
        List<Client> clients = clientRepository.findAllActiveByName(nome);
        return clients.stream()
                .map(clientMapper::toResponseDTO)
                .toList();
    }

    // Update
    @Transactional
    public ClientResponseDTO updateClient(Long id, ClientUpdateDTO dto) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client with id " + id + " not found"));

        if (!client.isActive()) {
            throw new BusinessException("Client inactive");
        }

        Optional<Client> clientWithSameEmail = clientRepository.findByEmail(dto.email());
        if (dto.email() != null && !dto.email().isEmpty() && clientWithSameEmail.isPresent() && !clientWithSameEmail.get().getId().equals(id)) {
            throw new DuplicateResourceException("Email not available");
        }

        Optional<Client> clientWithSamePhone = clientRepository.findByPhone(dto.phone());
        if (clientWithSamePhone.isPresent() && !clientWithSamePhone.get().getId().equals(id)) {
            throw new DuplicateResourceException("Phone not available");
        }

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
}