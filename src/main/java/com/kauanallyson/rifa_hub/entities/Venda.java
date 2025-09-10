package com.kauanallyson.rifa_hub.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "vendas")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(exclude = {"rifa", "pontos"})
public class Venda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome do cliente não pode estar em branco")
    @Size(min = 2, max = 100)
    @Column(nullable = false, length = 100)
    private String nomeCliente;

    @NotBlank(message = "O e-mail do cliente não pode estar em branco")
    @Email(message = "Formato de e-mail inválido.")
    @Column(nullable = false)
    private String emailCliente;

    @NotNull(message = "A compra deve estar associada a uma rifa")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "rifa_id", nullable = false)
    private Rifa rifa;

    @OneToMany(mappedBy = "venda", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Ponto> pontos = new ArrayList<>();

    public void addPonto(Ponto ponto) {
        this.pontos.add(ponto);
        ponto.setVenda(this);
    }

    public void removePonto(Ponto ponto) {
        this.pontos.remove(ponto);
        ponto.setVenda(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Venda venda = (Venda) o;
        return id != null && Objects.equals(id, venda.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClass());
    }
}