package com.kauanallyson.rifa_hub_api.seller;

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

    @Query(value = "SELECT * FROM sellers WHERE active = true", nativeQuery = true)
    List<Seller> findAllActive();

    @Query(value = """
            SELECT * FROM sellers
            WHERE active = true
            AND to_tsvector('simple', name) @@ websearch_to_tsquery('simple', :name)
            """, nativeQuery = true)
    List<Seller> searchActiveByName(@Param("name") String name);
}
