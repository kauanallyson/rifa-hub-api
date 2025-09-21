package com.kauanallyson.rifa_hub_api.entities;

import com.kauanallyson.rifa_hub_api.entities.enums.StatusPonto;
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
@Table(name = "pontos",
    uniqueConstraints = {
    @UniqueConstraint(columnNames = {"numero", "rifa_id"})
    })
public class Ponto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer numero;

    @Enumerated(EnumType.STRING)
    private StatusPonto status;

    private LocalDateTime dataVenda;

    @ManyToOne(optional = false)
    @JoinColumn(name = "rifa_id")
    @ToString.Exclude
    private Rifa rifa;

    @ManyToOne
    @JoinColumn(name = "comprador_id")
    @ToString.Exclude
    private Comprador comprador;

    @ManyToOne
    @JoinColumn(name = "vendedor_id")
    @ToString.Exclude
    private Vendedor vendedor;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Ponto ponto)) return false;
        return Objects.equals(id, ponto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
