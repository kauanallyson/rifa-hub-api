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
@Table(name = "premios")
public class Premio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String descricao;

    @Column(nullable = false)
    private Integer colocacao;

    @ManyToOne(optional = false)
    @JoinColumn(name = "rifa_id")
    @ToString.Exclude
    private Rifa rifa;

    @OneToOne
    @JoinColumn(name = "ponto_vencedor_id")
    @ToString.Exclude
    private Ponto pontoVencedor;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Premio premio)) return false;
        return Objects.equals(id, premio.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
