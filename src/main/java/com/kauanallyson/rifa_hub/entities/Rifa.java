package com.kauanallyson.rifa_hub.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "rifas")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(exclude = {"vendedores", "vendas", "pontos", "sorteio"})
public class Rifa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome da rifa não pode estar em branco")
    @Size(min = 3, max = 150)
    @Column(nullable = false, length = 150)
    private String nome;

    @Size(max = 2000)
    @Column(length = 2000)
    private String descricao;

    @Positive(message = "O preço deve ser um valor positivo")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;

    @Min(value = 1, message = "A rifa deve ter no mínimo 1 número")
    @Column(nullable = false)
    private Integer numeroDePontos;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "vendedores_rifa",
            joinColumns = @JoinColumn(name = "rifa_id"),
            inverseJoinColumns = @JoinColumn(name = "vendedor_id")
    )
    private List<Vendedor> vendedores = new ArrayList<>();

    @OneToMany(mappedBy = "rifa", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Venda> vendas = new ArrayList<>();

    @OneToMany(mappedBy = "rifa", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Ponto> pontos = new ArrayList<>();

    @OneToOne(mappedBy = "rifa", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Sorteio sorteio;

    public void addVendedor(Vendedor vendedor) {
        this.vendedores.add(vendedor);
        vendedor.getRifas().add(this);
    }

    public void removeVendedor(Vendedor vendedor) {
        this.vendedores.remove(vendedor);
        vendedor.getRifas().remove(this);
    }

    public void addVenda(Venda venda) {
        this.vendas.add(venda);
        venda.setRifa(this);
    }

    public void removeVenda(Venda venda) {
        this.vendas.remove(venda);
        venda.setRifa(null);
    }

    public void addPonto(Ponto ponto) {
        this.pontos.add(ponto);
        ponto.setRifa(this);
    }

    public void setSorteio(Sorteio sorteio) {
        if (sorteio == null) {
            if (this.sorteio != null) {
                this.sorteio.setRifa(null);
            }
        } else {
            sorteio.setRifa(this);
        }
        this.sorteio = sorteio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rifa rifa = (Rifa) o;
        return id != null && Objects.equals(id, rifa.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClass());
    }
}