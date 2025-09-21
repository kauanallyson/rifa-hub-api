package com.kauanallyson.rifa_hub.entities;

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
@Table(name = "compradores")
public class Comprador {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String telefone;

    @Column(unique = true)
    private String email;

    @OneToMany(mappedBy = "comprador")
    @ToString.Exclude
    private List<Ponto> pontosComprados;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Comprador comprador)) return false;
        return Objects.equals(id, comprador.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
