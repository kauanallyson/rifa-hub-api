package com.kauanallyson.rifa_hub_api.repositories;

import com.kauanallyson.rifa_hub_api.entities.Rifa;
import com.kauanallyson.rifa_hub_api.entities.enums.StatusRifa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface RifaRepository extends JpaRepository<Rifa, Long>, JpaSpecificationExecutor<Rifa> {
    boolean existsByNome(String nome);

    Optional<Rifa> findByNome(String nome);

    Page<Rifa> findAllByNome(String nome);

    Page<Rifa> findAllByNomeContainingIgnoreCaseAndStatusIn(String nome, Collection<StatusRifa> statuses, Pageable pageable);

    Page<Rifa> findAllByStatus(StatusRifa status, Pageable pageable);

}