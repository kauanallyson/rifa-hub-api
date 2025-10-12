package com.kauanallyson.rifa_hub_api.entities;

import com.kauanallyson.rifa_hub_api.entities.enums.RaffleStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "raffles")
public class Raffle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String description;

    @OneToMany(mappedBy = "raffle", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("placing ASC")
    @ToString.Exclude
    private List<Prize> prizes;

    @Column(nullable = false)
    private BigDecimal ticketPrice;

    private LocalDateTime drawDate;

    @Column(nullable = false)
    private Integer ticketAmount;

    @OneToMany(mappedBy = "raffle", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Ticket> tickets;

    @Enumerated(EnumType.STRING)
    private RaffleStatus status;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Raffle raffle)) return false;
        return Objects.equals(id, raffle.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}