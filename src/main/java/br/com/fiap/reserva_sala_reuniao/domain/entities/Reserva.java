package br.com.fiap.reserva_sala_reuniao.domain.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.domain.AbstractAggregateRoot;

import br.com.fiap.reserva_sala_reuniao.domain.events.ReservaStatusAlteradoEvent;

@Entity
@Table(name = "tb_reservas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Reserva extends AbstractAggregateRoot<Reserva> {

    public enum StatusReserva { SOLICITADA, CONFIRMADA, CANCELADA }

    @Id
    @EqualsAndHashCode.Include
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sala_id", nullable = false)
    private Sala sala;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "data_inicio", nullable = false)
    private LocalDateTime dataInicio;

    @Column(name = "data_fim", nullable = false)
    private LocalDateTime dataFim;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusReserva status;

    public Reserva(Sala sala, Usuario usuario, LocalDateTime dataInicio, LocalDateTime dataFim) {
        if (dataInicio.isAfter(dataFim) || dataInicio.isEqual(dataFim)) {
            throw new IllegalArgumentException("A data de início deve ser anterior à data de fim.");
        }

        this.id = UUID.randomUUID().toString();
        this.sala = sala;
        this.usuario = usuario;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.status = StatusReserva.SOLICITADA;
    }

    public void confirmar() {
        if (this.status == StatusReserva.CANCELADA) {
            throw new IllegalStateException("Não é possível confirmar uma reserva cancelada.");
        }

        this.status = StatusReserva.CONFIRMADA;

        registrarEventoEnvioEmail();
    }

    public void cancelar() {
        this.status = StatusReserva.CANCELADA;

        registrarEventoEnvioEmail();
    }

    public void registrarEventoEnvioEmail() {
        registerEvent(new ReservaStatusAlteradoEvent(
            this.usuario.getEmail(), this.usuario.getNome(), this.sala.getNome(), this.status, this.dataInicio, this.dataFim
        ));
    }
}
