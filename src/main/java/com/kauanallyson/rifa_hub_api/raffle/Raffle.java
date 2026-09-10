package com.kauanallyson.rifa_hub_api.raffle;

import com.kauanallyson.rifa_hub_api.prize.Prize;
import com.kauanallyson.rifa_hub_api.shared.enums.RaffleStatus;
import com.kauanallyson.rifa_hub_api.ticket.Ticket;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "raffles")
@EqualsAndHashCode(of = "id")
public class Raffle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String description;

    @OneToMany(mappedBy = "raffle", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("placement ASC")
    @ToString.Exclude
    private List<Prize> prizes;

    @Column(nullable = false)
    private BigDecimal ticketPrice;

    private LocalDateTime drawDate;

    @Column(nullable = false)
    private Long ticketAmount;

    @OneToMany(mappedBy = "raffle", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Ticket> tickets;

    @Enumerated(EnumType.STRING)
    private RaffleStatus status;
}