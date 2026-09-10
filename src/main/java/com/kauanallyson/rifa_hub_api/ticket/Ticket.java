package com.kauanallyson.rifa_hub_api.ticket;

import com.kauanallyson.rifa_hub_api.client.Client;
import com.kauanallyson.rifa_hub_api.raffle.Raffle;
import com.kauanallyson.rifa_hub_api.seller.Seller;
import com.kauanallyson.rifa_hub_api.shared.enums.TicketStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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
@EqualsAndHashCode(of = "id")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long number;

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
}
