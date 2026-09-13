package br.com.fiap.reserva_sala_reuniao.domain.events;

import java.time.LocalDateTime;

import br.com.fiap.reserva_sala_reuniao.domain.entities.Reserva.StatusReserva;

public record ReservaStatusAlteradoEvent(
    String usuarioEmail,
    String usuarioNome,
    String salaNome,
    StatusReserva novoStatus,
    LocalDateTime dataInicio,
    LocalDateTime dataFim
) {}
