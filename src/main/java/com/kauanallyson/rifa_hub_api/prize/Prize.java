package com.kauanallyson.rifa_hub_api.prize;

import com.kauanallyson.rifa_hub_api.raffle.Raffle;
import com.kauanallyson.rifa_hub_api.ticket.Ticket;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "prizes",
        indexes = {
                @Index(name = "idx_prizes_raffle", columnList = "raffle_id")
        })
@EqualsAndHashCode(of = "id")
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
}
