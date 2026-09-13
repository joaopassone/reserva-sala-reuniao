package br.com.fiap.reserva_sala_reuniao.infrastructure.web.dtos;

import java.time.LocalDateTime;

public record AlterarReservaRequestDTO(
    LocalDateTime inicio,
    LocalDateTime fim
) {}
