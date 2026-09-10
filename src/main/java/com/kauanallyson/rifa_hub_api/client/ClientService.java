package com.kauanallyson.rifa_hub_api.client;

import com.kauanallyson.rifa_hub_api.client.dtos.ClientCreate;
import com.kauanallyson.rifa_hub_api.client.dtos.ClientResponse;
import com.kauanallyson.rifa_hub_api.client.dtos.ClientUpdate;
import com.kauanallyson.rifa_hub_api.shared.exceptions.BusinessException;
import com.kauanallyson.rifa_hub_api.shared.exceptions.DuplicateResourceException;
import com.kauanallyson.rifa_hub_api.shared.exceptions.ResourceNotFoundException;
import org.springframework.util.StringUtils;
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
    public ClientResponse createClient(ClientCreate req) {
        if (clientRepository.existsByEmail(req.email())) {
            throw new DuplicateResourceException("Email not available");
        }

        if (clientRepository.existsByPhone(req.phone())) {
            throw new DuplicateResourceException("Phone not available");
        }

        Client client = clientMapper.toEntity(req);
        Client savedClient = clientRepository.save(client);
        return clientMapper.toResponseDTO(savedClient);
    }

    // Find all
    @Transactional(readOnly = true)
    public List<ClientResponse> findAll(String name) {
        List<Client> clients = StringUtils.hasText(name)
                ? clientRepository.searchActiveByName(name)
                : clientRepository.findAllActive();

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
    public ClientResponse updateClient(Long id, ClientUpdate req) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client with id " + id + " not found"));

        if (!client.isActive()) {
            throw new BusinessException("Client inactive");
        }

        updateEmailOrThrow(id, req.email());
        updatePhoneOrThrow(id, req.phone());

        clientMapper.updateEntityFromDTO(req, client);
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
    private void updateEmailOrThrow(Long currentId, String email) {
        if (email.isBlank()) {
            throw new BusinessException("Email is empty");
        }
        Optional<Client> conflict = clientRepository.findByEmail(email);
        if (conflict.isPresent() && !conflict.get().getId().equals(currentId)) {
            throw new DuplicateResourceException("Email not available");
        }
    }

    private void updatePhoneOrThrow(Long currentId, String phone) {
        if (phone.isBlank()) {
            throw new BusinessException("Phone is empty");
        }
        Optional<Client> conflict = clientRepository.findByPhone(phone);
        if (conflict.isPresent() && !conflict.get().getId().equals(currentId)) {
            throw new DuplicateResourceException("Phone not available");
        }
    }
}