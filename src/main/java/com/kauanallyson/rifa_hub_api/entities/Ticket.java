package com.kauanallyson.rifa_hub_api.entities;

import com.kauanallyson.rifa_hub_api.entities.enums.TicketStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tickets",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"number", "raffle_id"})
        })
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer number;

    @Enumerated(EnumType.STRING)
    private TicketStatus status;

    private LocalDateTime saleDate;

    @ManyToOne(optional = false)
    @JoinColumn(name = "raffle_id")
    @ToString.Exclude
    private Raffle raffle;

    @ManyToOne
    @JoinColumn(name = "client_id")
    @ToString.Exclude
    private Client client;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    @ToString.Exclude
    private Seller seller;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Ticket ticket)) return false;
        return Objects.equals(id, ticket.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
