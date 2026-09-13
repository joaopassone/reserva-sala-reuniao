package br.com.fiap.reserva_sala_reuniao.infrastructure.web.dtos;

import java.time.LocalDateTime;

public record ReservaRequestDTO(
    String salaId,
    LocalDateTime inicio,
    LocalDateTime fim
) {}
