package com.kauanallyson.rifa_hub.entities;

import com.kauanallyson.rifa_hub.entities.enums.SorteioStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "sorteios")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(exclude = {"rifa", "pontoVencedor"})
public class Sorteio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @NotNull(message = "A data do sorteio é obrigatória")
    @Column(nullable = false)
    private LocalDateTime dataSorteio;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SorteioStatus status;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "rifa_id", nullable = false, unique = true)
    private Rifa rifa;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ponto_vencedor_id", unique = true)
    private Ponto pontoVencedor;

    public Sorteio(Rifa rifa, LocalDateTime dataSorteio) {
        this.rifa = rifa;
        this.dataSorteio = dataSorteio;
        this.status = SorteioStatus.AGENDADO;
    }

    public void performSorteio(Ponto pontoVencedor) {
        if (this.status != SorteioStatus.AGENDADO) {
            throw new IllegalStateException("O sorteio só pode ser realizado se estiver no status 'AGENDADO'. Status atual: " + this.status);
        }
        if (pontoVencedor == null) {
            throw new IllegalArgumentException("O número vencedor não pode ser nulo");
        }
        if (!pontoVencedor.getRifa().equals(this.rifa)) {
            throw new IllegalArgumentException("O número vencedor não pertence a esta rifa");
        }

        this.pontoVencedor = pontoVencedor;
        this.status = SorteioStatus.COMPLETO;
    }

    public void cancel() {
        if (this.status == SorteioStatus.COMPLETO) {
            throw new IllegalStateException("Não é possível cancelar um sorteio que já foi realizado.");
        }
        this.status = SorteioStatus.CANCELADO;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sorteio sorteio = (Sorteio) o;
        return rifa != null && Objects.equals(rifa, sorteio.rifa);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rifa);
    }
}