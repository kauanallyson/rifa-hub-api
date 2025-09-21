package com.kauanallyson.rifa_hub_api.entities;

import com.kauanallyson.rifa_hub_api.entities.enums.StatusRifa;
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
@Table(name = "rifas")
public class Rifa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    private String descricao;

    @OneToMany(mappedBy = "rifa", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("colocacao ASC")
    @ToString.Exclude
    private List<Premio> premios;

    @Column(nullable = false)
    private BigDecimal pontoPreco;

    @Column(nullable = false)
    private Integer quantidadePontos;

    private LocalDateTime dataSorteio;

    @OneToMany(mappedBy = "rifa", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Ponto> pontos;

    @Enumerated(EnumType.STRING)
    private StatusRifa status;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Rifa rifa)) return false;
        return Objects.equals(id, rifa.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}