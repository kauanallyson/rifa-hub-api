package com.kauanallyson.rifa_hub_api.entities;

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
@Table(name = "vendedores")
public class Vendedor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String telefone;

    @Column(unique = true)
    private String email;

    @OneToMany(mappedBy = "vendedor")
    @ToString.Exclude
    private List<Ponto> pontosVendidos;

    private boolean ativo = true;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Vendedor vendedor)) return false;
        return Objects.equals(id, vendedor.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
