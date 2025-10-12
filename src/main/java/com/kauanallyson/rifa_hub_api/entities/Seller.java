package com.kauanallyson.rifa_hub_api.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sellers")
public class Seller {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String phone;

    @Column(unique = true)
    private String email;

    @OneToMany(mappedBy = "seller")
    @ToString.Exclude
    private List<Ticket> ticketsSold;

    private boolean active = true;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Seller seller)) return false;
        return Objects.equals(id, seller.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
