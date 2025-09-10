package com.kauanallyson.rifa_hub.services;

import com.kauanallyson.rifa_hub.exceptions.ResourceNotFoundException;
import com.kauanallyson.rifa_hub.entities.Rifa;
import com.kauanallyson.rifa_hub.repositories.RifaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RifaService {
    private final RifaRepository rifaRepository;

    public RifaService(RifaRepository rifaRepository) {
        this.rifaRepository = rifaRepository;
    }

    @Transactional
    public Rifa createRifa(Rifa rifa) {
        return rifaRepository.save(rifa);
    }

    @Transactional(readOnly = true)
    public List<Rifa> findAllRifas() {
        return rifaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Rifa findRifaById(Long id) {
        return rifaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Rifa com id " + id + " não encontrada"));
    }

    @Transactional
    public Rifa updateRifa(Long id, Rifa rifaDetails) {
        Rifa existingRifa = findRifaById(id);

        existingRifa.setNome(rifaDetails.getNome());
        existingRifa.setDescricao(rifaDetails.getDescricao());
        existingRifa.setPreco(rifaDetails.getPreco());
        existingRifa.setNumeroDePontos(rifaDetails.getNumeroDePontos());

        return rifaRepository.save(existingRifa);
    }

    @Transactional
    public void deleteRifa(Long id) {
        if (!rifaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Rifa com id " + id + " não encontrada");
        }
        rifaRepository.deleteById(id);
    }
}
