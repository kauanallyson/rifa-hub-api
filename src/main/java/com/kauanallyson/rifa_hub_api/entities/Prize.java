package com.kauanallyson.rifa_hub_api.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "prizes")
public class Prize {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Integer placement;

    @ManyToOne(optional = false)
    @JoinColumn(name = "raffle_id")
    @ToString.Exclude
    private Raffle raffle;

    @OneToOne
    @JoinColumn(name = "winning_ticket_id")
    @ToString.Exclude
    private Ticket winningTicket;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Prize prize)) return false;
        return Objects.equals(id, prize.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
