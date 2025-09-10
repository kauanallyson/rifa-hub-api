package com.kauanallyson.rifa_hub.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "vendedores")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(exclude = {"pontos", "rifas"})
public class Vendedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome não pode estar em branco.")
    @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres.")
    @Column(nullable = false, length = 100)
    private String nome;

    @NotBlank(message = "O e-mail não pode estar em branco.")
    @Email(message = "Formato de e-mail inválido.")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "O telefone не pode estar em branco.")
    @Pattern(regexp = "^[1-9]{2} [9]{1}[0-9]{4}-[0-9]{4}$", message = "Formato de telefone inválido.")
    @Column(nullable = false, unique = true)
    private String telefone;

    @OneToMany(mappedBy = "vendedor", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Ponto> pontos = new ArrayList<>();

    @ManyToMany(mappedBy = "vendedores",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            fetch = FetchType.LAZY)
    private List<Rifa> rifas = new ArrayList<>();

    public void addRifa(Rifa rifa) {
        this.rifas.add(rifa);
        rifa.getVendedores().add(this);
    }

    public void removeRifa(Rifa rifa) {
        this.rifas.remove(rifa);
        rifa.getVendedores().remove(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vendedor vendedor = (Vendedor) o;
        return email != null && Objects.equals(email, vendedor.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}