package com.kauanallyson.rifa_hub_api.repositories;

import com.kauanallyson.rifa_hub_api.entities.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SellerRepository extends JpaRepository<Seller, Long> {
    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    Optional<Seller> findByEmail(String email);

    Optional<Seller> findByPhone(String phone);

    @Query("SELECT s FROM Seller s WHERE s.id = :id AND s.active = true")
    Optional<Seller> findActiveById(@Param("id") Long id);

    @Query("""
            SELECT s FROM Seller s
            WHERE (:name IS NULL OR (LOWER(s.name) LIKE :name))
            AND s.active = true
            """)
    List<Seller> findAllActiveByName(@Param("name") String name);
}
