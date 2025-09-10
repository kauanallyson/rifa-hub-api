package com.kauanallyson.rifa_hub.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "pontos", uniqueConstraints = {@UniqueConstraint(columnNames = {"numero", "rifa_id"})})
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = {"vendedor", "venda", "rifa"})
public class Ponto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @PositiveOrZero(message = "O número da rifa não pode ser negativo.")
    @Column(nullable = false)
    private Integer numero;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime dataHoraVenda;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vendedor_id", nullable = false)
    private Vendedor vendedor;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "venda_id", nullable = false)
    private Venda venda;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "rifa_id", nullable = false)
    private Rifa rifa;

    public Ponto(Integer numero, Vendedor vendedor, Venda venda) {
        if (venda.getRifa() == null) {
            throw new IllegalStateException("A Venda deve estar associada a uma Rifa");
        }
        this.numero = numero;
        this.vendedor = vendedor;
        this.venda = venda;
        this.rifa = venda.getRifa();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ponto that = (Ponto) o;
        return Objects.equals(rifa, that.rifa) && Objects.equals(numero, that.numero);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rifa, numero);
    }
}